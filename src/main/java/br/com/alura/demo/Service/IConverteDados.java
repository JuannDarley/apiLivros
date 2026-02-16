package br.com.alura.demo.Service;

public interface IConverteDados {
    <T> T  obterDados(String json, Class<T> classe);
}
