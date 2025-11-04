package br.com.fiap.mottu_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import br.com.fiap.mottu_api.repository.MotoRepository;
import br.com.fiap.mottu_api.repository.PatioRepository;
import br.com.fiap.mottu_api.repository.UsuarioPatioRepository;

@SpringBootTest
@ActiveProfiles("test")
class MottuApiApplicationTests {

	@MockBean
	private UsuarioPatioRepository usuarioPatioRepository;

	@MockBean
	private MotoRepository motoRepository;

	@MockBean
	private PatioRepository patioRepository;

	@Test
	void contextLoads() {
	}

}
