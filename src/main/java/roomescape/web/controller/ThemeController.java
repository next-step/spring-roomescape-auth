package roomescape.web.controller;

import java.net.URI;
import java.util.List;

import roomescape.service.ThemeService;
import roomescape.web.controller.dto.ThemeRequest;
import roomescape.web.controller.dto.ThemeResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/themes")
public class ThemeController {

	private final ThemeService themeService;

	ThemeController(ThemeService themeService) {
		this.themeService = themeService;
	}

	@GetMapping
	public ResponseEntity<List<ThemeResponse>> getThemes() {
		return ResponseEntity.ok().body(this.themeService.getThemes());
	}

	@PostMapping
	public ResponseEntity<ThemeResponse> create(@RequestBody ThemeRequest request) {
		ThemeRequest.validateThemeRequest(request);
		ThemeResponse response = this.themeService.create(request);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();

		return ResponseEntity.created(location).body(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") long id) {
		this.themeService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
