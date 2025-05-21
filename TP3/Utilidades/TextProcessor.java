package Utilidades;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TextProcessor {

    private static final Set<String> STOP_WORDS = new HashSet<>();

    static {
        STOP_WORDS.add("a");
        STOP_WORDS.add("o");
        STOP_WORDS.add("as");
        STOP_WORDS.add("os");
        STOP_WORDS.add("e");
        STOP_WORDS.add("de");
        STOP_WORDS.add("do");
        STOP_WORDS.add("da");
        STOP_WORDS.add("dos");
        STOP_WORDS.add("das");
        STOP_WORDS.add("em");
        STOP_WORDS.add("um");
        STOP_WORDS.add("uma");
        STOP_WORDS.add("para");
        STOP_WORDS.add("com");
        STOP_WORDS.add("no");
        STOP_WORDS.add("na");
        STOP_WORDS.add("nos");
        STOP_WORDS.add("nas");
        STOP_WORDS.add("por");
        STOP_WORDS.add("é");
        STOP_WORDS.add("são");
        STOP_WORDS.add("que");
        STOP_WORDS.add("um");
        STOP_WORDS.add("uma");
    }

    // Normaliza o texto: remove acentos, converte para minúsculas
    public static String normalizarTexto(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}\\s]", "") // Mantém caracteres ASCII e espaços
                .toLowerCase();
    }

    // Quebra a frase em palavras
    public static String[] tokenizarEFiltrar(String texto) {
        String textoNormalizado = normalizarTexto(texto);
        String[] palavras = textoNormalizado.split("\\s+"); // Divide por um ou mais espaços
        ArrayList<String> palavrasFiltradas = new ArrayList<>();
        for (String palavra : palavras) {
            if (!palavra.isEmpty() && !STOP_WORDS.contains(palavra)) {
                palavrasFiltradas.add(palavra);
            }
        }
        return palavrasFiltradas.toArray(new String[0]);
    }
}