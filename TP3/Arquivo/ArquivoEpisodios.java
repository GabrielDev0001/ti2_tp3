package Arquivo;

import aed3.*;
import Entidades.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArquivoEpisodios extends Arquivo<Episodio> {

  ArvoreBMais<ParIdId> indiceIdEpiIdSerie;
  ListaInvertida indiceInvertidoNomeEpisodio;

  public ArquivoEpisodios() throws Exception {
    super("episodio", Episodio.class.getConstructor());

    new File("./dados/episodio").mkdirs();

    indiceIdEpiIdSerie = new ArvoreBMais<>(
      ParIdId.class.getConstructor(),
      5,
      "./dados/episodio/indiceIdEpisodios_IdSerie.db"
    );

    indiceInvertidoNomeEpisodio = new ListaInvertida(
      5,
      "./dados/episodio/indiceInvertidoNomeEpisodio.db",
      "./dados/episodio/blocosNomeEpisodio.db"
    );
  }

  
  public int createEpi(Episodio e) throws Exception {
    if (!ArquivoSeries.serieExiste(e.getIDSerie()))
      throw new Exception("Episodio não pode ser criado pois a serie vinculada não existe");

    int id = super.create(e);
    indiceIdEpiIdSerie.create(new ParIdId(e.getIDSerie(), id));

    String[] termos = TextProcessor.tokenizarEFiltrar(e.getNome());
    Map<String, Integer> frequencias = new HashMap<>();
    for (String termo : termos) {
      frequencias.put(termo, frequencias.getOrDefault(termo, 0) + 1);
    }
    for (Map.Entry<String, Integer> entrada : frequencias.entrySet()) {
      indiceInvertidoNomeEpisodio.create(entrada.getKey(), new ElementoLista(id, (float) entrada.getValue()));
    }

    return id;
  }

  public Episodio[] readNomeEpisodio(String consulta) throws Exception {
    String[] termos = TextProcessor.tokenizarEFiltrar(consulta);
    Map<Integer, Float> ranking = new HashMap<>();

    for (String termo : termos) {
      ElementoLista[] elementos = indiceInvertidoNomeEpisodio.read(termo);
      if (elementos != null) {
        for (ElementoLista el : elementos) {
          ranking.put(el.getId(), ranking.getOrDefault(el.getId(), 0f) + el.getFrequencia());
        }
      }
    }

    ArrayList<Map.Entry<Integer, Float>> ordenado = new ArrayList<>(ranking.entrySet());
    ordenado.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()));

    ArrayList<Episodio> resultado = new ArrayList<>();
    for (Map.Entry<Integer, Float> entrada : ordenado) {
      Episodio epi = read(entrada.getKey());
      if (epi != null) resultado.add(epi);
    }

    return resultado.toArray(new Episodio[0]);
  }

  public Episodio[] lerNomeEpisodioPorSerie(String nome, int idSerie) throws Exception {
    Episodio[] episodios = readNomeEpisodio(nome);
    if (episodios == null) return null;

    ArrayList<Episodio> filtrados = new ArrayList<>();
    for (Episodio e : episodios) {
      if (e.getIDSerie() == idSerie) filtrados.add(e);
    }

    return filtrados.toArray(new Episodio[0]);
  }

  public Episodio[] readEpisodiosSerie(int idSerie) throws Exception {
    ArrayList<ParIdId> locs = indiceIdEpiIdSerie.read(new ParIdId(idSerie, -1));

    if (locs.size() > 0) {
      Episodio[] episodios = new Episodio[locs.size()];
      int i = 0;
      for (ParIdId loc : locs) {
        episodios[i++] = read(loc.getId_agregado());
      }
      return episodios;
    } else {
      return null;
    }
  }

  @Override
  public boolean delete(int id) throws Exception {
    Episodio e = read(id);
    if (e != null && super.delete(id)) {
      indiceIdEpiIdSerie.delete(new ParIdId(e.getIDSerie(), id));

      String[] termos = TextProcessor.tokenizarEFiltrar(e.getNome());
      for (String termo : termos) {
        indiceInvertidoNomeEpisodio.delete(termo, id);
      }
      return true;
    }
    return false;
  }

  public boolean deleteEpisodioSerie(int idSerie) throws Exception {
    ArrayList<ParIdId> locs = indiceIdEpiIdSerie.read(new ParIdId(idSerie, -1));
    if (locs.size() > 0) {
      for (ParIdId loc : locs)
        delete(loc.getId_agregado());
      return true;
    }
    return false;
  }

  @Override
  public boolean update(Episodio novoEpi) throws Exception {
    Episodio antigo = read(novoEpi.getId());
    if (antigo != null && super.update(novoEpi)) {
      if (!antigo.getNome().equals(novoEpi.getNome())) {
        String[] termosAntigos = TextProcessor.tokenizarEFiltrar(antigo.getNome());
        for (String termo : termosAntigos) {
          indiceInvertidoNomeEpisodio.delete(termo, antigo.getId());
        }

        String[] novosTermos = TextProcessor.tokenizarEFiltrar(novoEpi.getNome());
        Map<String, Integer> freq = new HashMap<>();
        for (String termo : novosTermos)
          freq.put(termo, freq.getOrDefault(termo, 0) + 1);

        for (Map.Entry<String, Integer> entry : freq.entrySet()) {
          indiceInvertidoNomeEpisodio.create(entry.getKey(), new ElementoLista(novoEpi.getId(), entry.getValue()));
        }
      }

      if (antigo.getIDSerie() != novoEpi.getIDSerie()) {
        indiceIdEpiIdSerie.delete(new ParIdId(antigo.getIDSerie(), antigo.getId()));
        indiceIdEpiIdSerie.create(new ParIdId(novoEpi.getIDSerie(), novoEpi.getId()));
      }

      return true;
    }
    return false;
  }

  public float avaliacaoMediaSerie(int idSerie) throws Exception {
    float soma = 0;
    ArrayList<ParIdId> locs = indiceIdEpiIdSerie.read(new ParIdId(idSerie, -1));
    if (locs.size() > 0) {
      Episodio[] episodios = new Episodio[locs.size()];
      int i = 0;
      for (ParIdId loc : locs) {
        episodios[i++] = read(loc.getId_agregado());
        soma += episodios[i - 1].getAvaliacao();
      }
      return soma / episodios.length;
    } else
      return 0;
  }

}
