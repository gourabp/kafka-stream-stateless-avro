package com.magicalpipelines;

import com.magicalpipelines.language.LanguageClient;
import com.magicalpipelines.serialization.Tweet;
import com.magicalpipelines.serialization.json.TweetSerdes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.util.Arrays;
import java.util.List;

public class MCryptoTopology {

    private static final List<String> currencies = Arrays.asList("bitcoin", "ethereum");

    public static Topology build() {

        LanguageClient languageClient = new LangClient();

        StreamsBuilder builder = new StreamsBuilder();

        KStream<byte[], Tweet> stream = builder.stream("tweets", Consumed.with(Serdes.ByteArray(),new TweetSerdes()));

        System.out.println("Raw messages start");
        stream.print(Printed.<byte[], Tweet>toSysOut().withLabel("tweets-stream"));
        System.out.println("Raw messages end");

        KStream<byte[], Tweet> filtered =
                stream.filter(
                        (key, tweet) -> {
                            return !tweet.isRetweet();
                        });

        System.out.println("filtered messages start");
        stream.print(Printed.<byte[], Tweet>toSysOut().withLabel("filtered-stream"));
        System.out.println("filtered messages end");

        //--we can try to move them as class level and see if works
        Predicate<byte[], Tweet> englishTweets = (key, tweet) -> tweet.getLang().equals("en");
        Predicate<byte[], Tweet> nonEnglishTweets = (key, tweet) -> !tweet.getLang().equals("en");

        KStream<byte[], Tweet>[] branches = filtered.branch(englishTweets, nonEnglishTweets);

        KStream<byte[], Tweet> englishStream = branches[0];
        englishStream.print(Printed.<byte[], Tweet>toSysOut().withLabel("tweets-english"));

        KStream<byte[], Tweet> nonEnglishStream = branches[1];
        nonEnglishStream.print(Printed.<byte[], Tweet>toSysOut().withLabel("tweets-non-english"));

        // for non-English tweets, translate the tweet text first.
        KStream<byte[], Tweet> translatedStream =
                nonEnglishStream.mapValues(
                        (tweet) -> {
                            return languageClient.translate(tweet, "en");
                        });

        translatedStream.print(Printed.<byte[], Tweet>toSysOut().withLabel("translatedStream"));

        KStream<byte[], Tweet> merged = englishStream.merge(translatedStream);


        merged.print(Printed.<byte[], Tweet>toSysOut().withLabel("mergedStream"));


       /* KStream<byte[], EntitySentiment> enriched =
                merged.flatMapValues(
                        (tweet) -> {
                            List<EntitySentiment> results = languageClient.getEntitySentiment(tweet);

                            results.removeIf(
                                    entitySentiment -> !currencies.contains(entitySentiment.getEntity()));

                            return results;
                        });*/

           merged.to("crypto-sentiment",Produced.with(Serdes.ByteArray(),new TweetSerdes()));

        return builder.build();
    }
}
