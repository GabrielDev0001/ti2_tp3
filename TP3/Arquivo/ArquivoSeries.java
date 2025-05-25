package Arquivo;
import Entidades.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aed3.*;

public class ArquivoSeries extends Arquivo<Series> {
    
    ListaInvertida indiceInvertidoNomeSerie;
    ArvoreBMais<ParIdId> indiceIdAtor_IdSerie;
    ArvoreBMais<ParIdId> indiceIdSerie_IdAtor;

    public ArquivoSeries() throws Exception {
        super("series", Series.class.getConstructor());
        
        new File("./dados/series").mkdirs();
        new File("./dados/ator").mkdirs();

        indiceInvertidoNomeSerie = new ListaInvertida(
            5,
            "./dados/series/indiceInvertidoNomeSerie.db",
            "./dados/series/blocosNomeSerie.db"
        );

        indiceIdAtor_IdSerie = new ArvoreBMais<>(
            ParIdId.class.getConstructor(),
            5,
            "./dados/ator/indiceIdAtors_IdSerie.db"
        );

        indiceIdSerie_IdAtor = new ArvoreBMais<>(
            ParIdId.class.getConstructor(),
            5,
            "./dados/ator/indiceIdSeries_IdAtores.db"
        );
    }

    @Override
    public int create(Series s) throws Exception {
        int id = super.create(s);

        // Indexar nome da série na lista invertida
        String[] termos = TextProcessor.tokenizarEFiltrar(s.getNome());
        Map<String, Integer> frequencias = new HashMap<>();
        for (String termo : termos)
            frequencias.put(termo, frequencias.getOrDefault(termo, 0) + 1);
        
        for (Map.Entry<String, Integer> entrada : frequencias.entrySet()) {
            indiceInvertidoNomeSerie.create(entrada.getKey(), new ElementoLista(id, entrada.getValue()));
        }

        indiceIdAtor_IdSerie.create(new ParIdId(-1 , id));
        indiceIdSerie_IdAtor.create(new ParIdId(id , -1));
        return id;
    }

    public Series[] readNome(String consulta) throws Exception {
        String[] termos = TextProcessor.tokenizarEFiltrar(consulta);
        Map<Integer, Float> pontuacoes = new HashMap<>();

        for (String termo : termos) {
            ElementoLista[] elementos = indiceInvertidoNomeSerie.read(termo);
            if (elementos != null) {
                for (ElementoLista el : elementos) {
                    pontuacoes.put(el.getId(), pontuacoes.getOrDefault(el.getId(), 0f) + el.getFrequencia());
                }
            }
        }

        ArrayList<Map.Entry<Integer, Float>> ordenado = new ArrayList<>(pontuacoes.entrySet());
        ordenado.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()));

        ArrayList<Series> resultado = new ArrayList<>();
        for (Map.Entry<Integer, Float> entrada : ordenado) {
            Series s = read(entrada.getKey());
            if (s != null) resultado.add(s);
        }

        return resultado.toArray(new Series[0]);
    }

    public Series[] readAtoSeries(int id) throws Exception {
        if(id < 0)
            return null;

        ArrayList<ParIdId> ptis = indiceIdAtor_IdSerie.read(new ParIdId(id, -1));
        if(ptis.size() > 0) {
            Series[] series = new Series[ptis.size()];
            int i = 0;
            for(ParIdId pti: ptis) 
                series[i++] = read(pti.getId_agregado());
            return series;
        }
        else 
            return null;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Series s = read(id);
        if(s != null && super.delete(id)) {
            indiceIdAtor_IdSerie.delete(new ParIdId(-1 , id));
            indiceIdSerie_IdAtor.delete(new ParIdId(id , -1));

            String[] termos = TextProcessor.tokenizarEFiltrar(s.getNome());
            for (String termo : termos) {
                indiceInvertidoNomeSerie.delete(termo, id);
            }

            return true;
        }
        return false;
    }

    public boolean delete(String nome, int id) throws Exception {
        ArquivoEpisodios arqEpisodios = new ArquivoEpisodios();
        Series s = read(id); 
        if(s != null && s.getId() == id && s.getNome().equals(nome)) {
            Episodio[] episodios = arqEpisodios.readEpisodiosSerie(id); 
            if(episodios != null) {
                boolean deletados = arqEpisodios.deleteEpisodioSerie(id);
                if(deletados) return delete(id);
            }
            return delete(id);
        }
        return false;
    }

    @Override
    public boolean update(Series novaSerie) throws Exception {
        Series antiga = read(novaSerie.getId());
        if(antiga != null && super.update(novaSerie)) {
            if(!antiga.getNome().equals(novaSerie.getNome())) {
                String[] termosAntigos = TextProcessor.tokenizarEFiltrar(antiga.getNome());
                for (String termo : termosAntigos) {
                    indiceInvertidoNomeSerie.delete(termo, antiga.getId());
                }

                String[] termosNovos = TextProcessor.tokenizarEFiltrar(novaSerie.getNome());
                Map<String, Integer> freq = new HashMap<>();
                for (String termo : termosNovos)
                    freq.put(termo, freq.getOrDefault(termo, 0) + 1);
                for (Map.Entry<String, Integer> entry : freq.entrySet()) {
                    indiceInvertidoNomeSerie.create(entry.getKey(), new ElementoLista(novaSerie.getId(), entry.getValue()));
                }
            }
            return true;
        }
        return false;
    }

    public boolean updateAtor(Series novaSerie, Ator ator) throws Exception {
        Series s = read(novaSerie.getId());
        int a = ator.id;
        if(s != null && super.update(novaSerie)) {
            if(!s.getNome().equals(novaSerie.getNome())) {
                indiceIdAtor_IdSerie.delete(new ParIdId(a, s.getId()));
                indiceIdAtor_IdSerie.create(new ParIdId(a, s.getId()));
                indiceIdSerie_IdAtor.delete(new ParIdId(s.getId(), a));
                indiceIdSerie_IdAtor.create(new ParIdId(s.getId(), a));
            }
            return true;
        }
        return false;
    }

    public static boolean serieExiste(int id) throws Exception {
        ArquivoSeries arqSeries = new ArquivoSeries();
        Series s = arqSeries.read(id);
        return s != null;
    }
}
