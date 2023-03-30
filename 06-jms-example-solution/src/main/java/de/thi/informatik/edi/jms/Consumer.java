package de.thi.informatik.edi.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    Logger logger = LoggerFactory.getLogger(getClass());

    @JmsListener(destination = "local.inmemory.queue")
    public void onMessage(String content){
        logger.info("Message received : "+content);
    }
}