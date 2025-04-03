package br.com.EduardoPina.screenMatch.service;

public interface IConverteDados {

   <T> T ObterDados(String json, Class<T> classe);

}
