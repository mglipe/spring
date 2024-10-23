package br.com.alura.screenmatch;

import br.com.alura.screenmatch.principal.Principal;
import br.com.alura.screenmatch.repository.SerieRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	 //delego para o framework a responsabilidade de instânciar a classe
    @Autowired
    private SerieRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(this.repository);
		principal.exibeMenu();
	}
}
