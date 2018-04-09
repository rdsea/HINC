package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/test")
public class HelloController {

    @GetMapping("/")
    public ResponseEntity index() {
        return new ResponseEntity("Greetings from Spring Boot HINC!", HttpStatus.OK);
    }


    @GetMapping("/greeting")
    public ResponseEntity greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return new ResponseEntity("greeting", HttpStatus.OK);
    }


}
