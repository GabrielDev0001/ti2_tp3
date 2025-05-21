package Arquivo;

import aed3.*;
import Entidades.Ator;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArquivoAtor extends Arquivo<Ator>{
    ArvoreBMais <ParIdAtor> indiceNomeAtor;
    ArquivoSeries arqSeries;
    ListaInvertida indiceInvertidoNomeAtor;

    public ArquivoAtor() throws Exception {
    super("ator", Ator.class.getConstructor());

    new File("./dados/ator").mkdirs();

    indiceNomeAtor = new ArvoreBMais<>(
      ParIdAtor.class.getConstructor(),
        5,
     "./dados/ator/indiceNomeAtores.db"
    );

    indiceInvertidoNomeAtor = new ListaInvertida(
      5,
      "./dados/ator/indiceInvertidoNomeAtor.db",
      "./dados/ator/blocosNomeAtor.db"
    );
  }

    public int criarAtor(Ator a) throws Exception{
        int id = super.create(a);
        indiceNomeAtor.create(new ParIdAtor(a.getNome(), id));

        String[] termos = TextProcessor.tokenizarEFiltrar(a.getNome());
        Map<String, Integer> frequenciaTermos = new HashMap<>();

        for(String termo : termos) {
            frequenciaTermos.put(termo, frequenciaTermos.getOrDefault(termo, 0) + 1);
        }

        for(Map.Entry<String, Integer> entrada : frequenciaTermos.entrySet()) {
            indiceInvertidoNomeAtor.create(entrada.getKey(), new ElementoLista(id, (float) entrada.getValue()));
        }
    
        return id;
    }

    public Ator[] readNome(String consulta) throws Exception{
        String[] termosConsulta = TextProcessor.tokenizarEFiltrar(consulta);
        Map<Integer, Float> pontuacoesAtores = new HashMap<>();
        
        if(termosConsulta.length == 0) {
            return null;
        }

        for(String termo : termosConsulta) {
            ElementoLista[] elementos = indiceInvertidoNomeAtor.read(termo);
            if(elementos != null) {
                for(ElementoLista el : elementos) {
                    pontuacoesAtores.put(el.getId(), pontuacoesAtores.getOrDefault(el.getId(), 0f) + el.getFrequencia());
                }
            }
        }

        ArrayList<Map.Entry<Integer, Float>> atoresOrdenados = new ArrayList<>(pontuacoesAtores.entrySet());
        atoresOrdenados.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        ArrayList<Ator> resultadoAtores = new ArrayList<>();
        for(Map.Entry<Integer, Float> entrada : atoresOrdenados) {
            Ator ator = read(entrada.getKey());
            if(ator != null) {
                resultadoAtores.add(ator);
            }
        }
        return resultadoAtores.toArray(new Ator[0]);
    }

    public boolean delete(int id) throws Exception{
        Ator ator = read(id);
        if(ator != null) {
            if(super.delete(id)) {
                String[] termos = TextProcessor.tokenizarEFiltrar(ator.getNome());
                for(String termo : termos) {
                    indiceInvertidoNomeAtor.delete(termo, id);
                }
                return true;
            }
        }
        return false;
    }

    public boolean update (Ator a) throws Exception {
        Ator ator = read(a.getId());
        if(ator == null) {
            return false;
        }

        if(!ator.getNome().equalsIgnoreCase(a.getNome())) {
            String[] termosAntigos = TextProcessor.tokenizarEFiltrar(ator.getNome());

            for(String termo : termosAntigos) {
                indiceInvertidoNomeAtor.delete(termo, ator.getId());
            }

            String[] novosTermos = TextProcessor.tokenizarEFiltrar(a.getNome());
            Map<String, Integer> novasFrequenciasTermos = new HashMap<>();

            for(String termo : novosTermos) {
                novasFrequenciasTermos.put(termo, novasFrequenciasTermos.getOrDefault(termo, 0) + 1);
            }
            for(Map.Entry<String, Integer> entrada : novasFrequenciasTermos.entrySet()) {
                indiceInvertidoNomeAtor.create(entrada.getKey(), new ElementoLista(a.getId(), (float) entrada.getValue()));
            }
        }
        return super.update(a); 
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
