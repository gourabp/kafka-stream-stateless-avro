package com.magicalpipelines;

import com.google.common.base.Splitter;
import com.magicalpipelines.language.LanguageClient;
import com.magicalpipelines.model.EntitySentiment;
import com.magicalpipelines.serialization.Tweet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LangClient implements LanguageClient {

    @Override
    public Tweet translate(Tweet tweet, String targetLanguage) {
        tweet.setText("Translated: " + tweet.getText());
        return tweet;
    }

    public List<EntitySentiment> getEntitySentiment(Tweet tweet){
        List<EntitySentiment> results = new ArrayList<>();

        Iterable<String> words = Splitter.on(' ').split(tweet.getText().toLowerCase().replace("#", ""));
        for (String entity : words) {
            EntitySentiment entitySentiment = new EntitySentiment();
            entitySentiment.setCreatedAt(tweet.getCreatedAt());
            entitySentiment.setId(tweet.getId());
            entitySentiment.setEntity(entity);
            entitySentiment.setText(tweet.getText());
            entitySentiment.setSalience(randomDouble());
            entitySentiment.setSentimentScore(randomDouble());
            entitySentiment.setSentimentMagnitude(randomDouble());


            results.add(entitySentiment);
        }
        return results;
    }

    Double randomDouble(){
          return ThreadLocalRandom.current().nextDouble(0, 1);
    }
}
