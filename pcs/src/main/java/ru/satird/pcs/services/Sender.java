package ru.satird.pcs.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.satird.pcs.config.RabbitConfig;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.dto.AdVisibleDto;
import ru.satird.pcs.mapper.AdMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class Sender {

    private final RabbitTemplate rabbitTemplate;
    private final AdService adService;
    private final AdMapper adMapper;

    @Autowired
    public Sender(RabbitTemplate rabbitTemplate, AdService adService, AdMapper adMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.adService = adService;
        this.adMapper = adMapper;
    }

    public void sendMessage(String body, String subject, String recipient) {
        Map<String, String> map = new HashMap<>();
        map.put("body", body);
        map.put("subject", subject);
        map.put("recipient", recipient);
        rabbitTemplate.convertAndSend(RabbitConfig.MY_QUEUE_NAME, map);
        log.info("Sent '" + map + "'");
    }

    @Scheduled(cron = "0 0 0 */7 * *")
    public void sendWeeklyData() {
        List<Ad> adList = new ArrayList<>(adService.getLastSevenDays());
        final List<AdVisibleDto> adVisibleDtoList = adMapper.mapAdVisibleDtoList(adList);
        rabbitTemplate.convertAndSend(RabbitConfig.ROUTING_KEY, adVisibleDtoList);
        log.info("Sent '" + adVisibleDtoList + "'");
    }
}
