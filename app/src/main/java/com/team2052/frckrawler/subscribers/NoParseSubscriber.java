package com.team2052.frckrawler.subscribers;

public class NoParseSubscriber extends BaseDataSubscriber<Object, Object> {
    @Override
    public void parseData() {
        dataToBind = data;
    }
}
