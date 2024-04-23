package live.hi.services.ms365connector.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("es-dm")
public class SharePointController {
	
	@PostMapping( path = "/upload" )
	public void upload( ) {
		// TODO: 
	}
}
