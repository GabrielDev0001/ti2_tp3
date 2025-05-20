package Arquivo;
import aed3.*;
import Entidades.*;

import java.io.File;
import java.util.ArrayList;

public class ArquivoEpisodios extends Arquivo<Episodio> {

  Arquivo<Episodio> arqEpisodio;
  ArvoreBMais <ParIdId> indiceIdEpiIdSerie;
  ArvoreBMais <ParNomeIdEpi> indiceNomeEpisodio;

  public ArquivoEpisodios() throws Exception {
    super("episodio", Episodio.class.getConstructor());

    new File("./dados/episodio").mkdirs();

    indiceIdEpiIdSerie = new ArvoreBMais<>(
      ParIdId.class.getConstructor(),
      5,
      "./dados/episodio/indiceIdEpisodios_IdSerie.db"
    );

    indiceNomeEpisodio = new ArvoreBMais<>(
      ParNomeIdEpi.class.getConstructor(),
      5,
      "./dados/episodio/indiceNomeEpisodios.db"
    );
  }

  @Override
  public int create(Episodio e) throws Exception{


    if(ArquivoSeries.serieExiste(e.getIDSerie()) == false){
      throw new Exception("Episodio não pode ser criado pois a serie vinculada não existe");
    }

    int id = super.create(e);
    
    indiceIdEpiIdSerie.create(new ParIdId(e.getIDSerie() , id));
    indiceNomeEpisodio.create(new ParNomeIdEpi(e.getNome(), id));

    return id;
  }

  public Episodio[] readNomeEpisodio(String nome) throws Exception{
    if(nome.length() == 0)
      return null;

    ArrayList<ParNomeIdEpi> docs = indiceNomeEpisodio.read(new ParNomeIdEpi(nome, -1));
    if(docs.size() > 0){
      Episodio[] episodios = new Episodio[docs.size()];
      int i = 0;
      for(ParNomeIdEpi doc: docs)
        episodios[i++] = read(doc.getId());
      return episodios;

    }else
      return null;
  }

  public Episodio[] lerNomeEpisodioPorSerie(String nome, int id_serie) throws Exception{
    if(nome.length() == 0)
      return null;

    ArrayList<ParNomeIdEpi> docs = indiceNomeEpisodio.read(new ParNomeIdEpi(nome, -1));
    if(docs.size() > 0){
      Episodio[] episodios = new Episodio[docs.size()];
      int i = 0;
      for(ParNomeIdEpi doc: docs)
        episodios[i++] = read(doc.getId());

      ArrayList<Episodio> episodiosSerie = new ArrayList<>();

      // Verifica se o episodio pertence a serie
      for(Episodio e : episodios){
        if(e.getIDSerie() == id_serie)
          episodiosSerie.add(e);
      }

      return episodiosSerie.toArray(new Episodio[episodiosSerie.size()]);
    }else
      return null;
  }
  
  public Episodio[] readEpisodiosSerie(int id_serie) throws Exception{
    
    // Metodo para verificar se a serie vinculada ao episodio existe
    ArrayList<ParIdId> locs = indiceIdEpiIdSerie.read(new ParIdId(id_serie, -1));
  
    if(locs.size() > 0){
      Episodio[] episodios = new Episodio[locs.size()];
      int i = 0;
      for(ParIdId loc : locs){
        episodios[i++] = read(loc.getId_agregado());
      }
      return episodios;
    }else{
      return null;
    }
  }

  @Override
  public boolean delete(int id) throws Exception{
    Episodio e = read(id);
    if(e != null){
      if(super.delete(id))
        return indiceIdEpiIdSerie.delete(new ParIdId(e.getIDSerie(), id)) 
            && indiceNomeEpisodio.delete(new ParNomeIdEpi(e.getNome(), id));
    }
    return false;
  }

  public boolean deleteEpisodioSerie(int id_serie) throws Exception{

    // Metodo para verificar se a serie vinculada ao episodio existe ordem id_serie -1 é ao contrario?
    ArrayList<ParIdId> locs = indiceIdEpiIdSerie.read(new ParIdId(id_serie, -1));

    System.out.println("Quantidade de episódios da serie deletados: " + locs.size());

    if(locs.size() > 0){
      for(ParIdId loc : locs)
        delete(loc.getId_agregado());
      return true;
    } 
    return false;
  }

  @Override
  public boolean update(Episodio novoEpisodio) throws Exception{
    Episodio e = read(novoEpisodio.getId());
    if(e != null){
      if(super.update(novoEpisodio)){
        if(!e.getNome().equals(novoEpisodio.getNome())){
          indiceNomeEpisodio.delete(new ParNomeIdEpi(e.getNome(), e.getId()));
          indiceNomeEpisodio.create(new ParNomeIdEpi(novoEpisodio.getNome(), e.getId()));
        }

        if(e.getIDSerie() != novoEpisodio.getIDSerie()){
          indiceIdEpiIdSerie.delete(new ParIdId(e.getIDSerie(), e.getId()));
          indiceIdEpiIdSerie.create(new ParIdId(novoEpisodio.getIDSerie(), e.getId()));
        }

        return true;
      }
    }
    return false;
  }


 
  public float avaliacaoMediaSerie(int id_serie) throws Exception{
    
    float soma = 0;
    
    ArrayList<ParIdId> locs = indiceIdEpiIdSerie.read(new ParIdId(id_serie, -1));
    if(locs.size() > 0){
    Episodio[] episodios = new Episodio[locs.size()];
    int i = 0;
    for(ParIdId loc : locs){
        episodios[i++] = read(loc.getId_agregado());
        soma += episodios[i-1].getAvaliacao();
    }
        return soma / episodios.length;
    }else
      return 0;
}


}
