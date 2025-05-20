package Arquivo;

import aed3.*;
import Entidades.Ator;
import java.io.File;
import java.util.ArrayList;

public class ArquivoAtor extends Arquivo<Ator>{
    ArvoreBMais <ParIdAtor> indiceNomeAtor;

    ArquivoSeries arqSeries;

    public ArquivoAtor() throws Exception {
    super("ator", Ator.class.getConstructor());

    new File("./dados/ator").mkdirs();

    indiceNomeAtor = new ArvoreBMais<>(
      ParIdAtor.class.getConstructor(),
      5,
      "./dados/episodio/indiceNomeEpisodios.db"
    );
  }

    public int criarAtor(Ator a) throws Exception{
        int id = super.create(a);
        indiceNomeAtor.create(new ParIdAtor(a.getNome(), id));
        return id;
    }

    public Ator[] readNome(String nome) throws Exception{
        ArrayList<ParIdAtor> docs = indiceNomeAtor.read(new ParIdAtor(nome, -1));
        if (docs.size() > 0) {
            Ator[] atores = new Ator[docs.size()];
            int i = 0;

            for (ParIdAtor doc : docs)
                atores[i++] = read(doc.getId());
            return atores;
        } else
            return null;
    }

    public boolean excluirAtor(int id) throws Exception{
        Ator ator = read(id);
        if(ator != null) {
            if(super.delete(id)) {
                return indiceNomeAtor.delete(new ParIdAtor(ator.getNome(), id));
            }
        }
        return false;
    }

    public boolean excluirAtor(String nome, int id) {
        return false;   
    }

    public boolean atualizarAtor(Ator a) throws Exception {
        Ator ator = read(a.getId());
        if(super.update(a)) {
            if(!ator.getNome().equals(a.getNome())) {
                indiceNomeAtor.delete(new ParIdAtor(ator.getNome(), ator.getId()));
                indiceNomeAtor.create(new ParIdAtor(a.getNome(), ator.getId()));
            }
            return true;
        }
        return false;
    }

    public Ator[] readSeriesAtor(int id) throws Exception {
        arqSeries = new ArquivoSeries();
        if(id < 0)
            return null;
            
        ArrayList<ParIdId> ptis = arqSeries.indiceIdSerie_IdAtor.read(new ParIdId(id, -1));
        if(ptis.size()>0) {
            Ator[] atores = new Ator[ptis.size()];
            int i=0;
            
            for(ParIdId pti: ptis) 
                atores[i++] = read(pti.getId_agregado());
            return atores;
        }
        else 
            return null;
    }


    public boolean atorExiste(int id) throws Exception{
        Ator a = read(id);
        if(a != null) {
            return true;
        }
        return false;
    }

}
