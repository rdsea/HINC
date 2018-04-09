package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;

@RestController
@RequestMapping("/test")
public class HelloController {
    @Value("${hinc.global.rabbitmq.input}")
    private String globalInputExchange;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/")
    public ResponseEntity index() {
        return new ResponseEntity("Greetings from Spring Boot HINC!", HttpStatus.OK);
    }


    @GetMapping("/test")
    public ResponseEntity test() {
        HincMessage testMessage = new HincMessage();
        testMessage.setMsgType(HINCMessageType.SYN_REPLY);
        testMessage.setSenderID("id");
        testMessage.setDestination("group","group");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] message = objectMapper.writeValueAsBytes(testMessage);
            rabbitTemplate.convertAndSend(globalInputExchange, "", message);
            return new ResponseEntity("sent \n" + testMessage.toJson(), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity("fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @GetMapping("/greeting")
    public ResponseEntity greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return new ResponseEntity("greeting", HttpStatus.OK);
    }


}
