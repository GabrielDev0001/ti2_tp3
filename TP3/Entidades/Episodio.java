package Entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import aed3.Registro;

public class Episodio implements Registro{
    public int id;
    public String nome;
    public float duracao; 
    public int temporada; 
    public int idSerie;
    public float avaliacao;
    public LocalDate dataLancamento;

    public Episodio( String nome, LocalDate dataLancamento,float avaliacao, float duracao, int temporada, int idSerie) {
        this.nome = nome;
        this.dataLancamento = dataLancamento;
        this.duracao = duracao;
        this.temporada = temporada;
        this.avaliacao = avaliacao;
        this.idSerie = idSerie;
    }

    public Episodio() {
        this.nome = "";
        this.dataLancamento = LocalDate.of(1900, 1, 1);
        this.duracao = 0.0f;
        this.temporada = 0;
        this.avaliacao = 0.0f;
        this.idSerie = 0;
    }

    public Episodio(int id, String nome, int temporada, LocalDate dataLancamento, float duracao) throws Exception {
        this.id = id;
        if (nome.length() > 0 && nome.length() <= 50)
            this.nome = nome;
        else
            throw new Exception("Nome inválido! Tamanho deve ser entre 1 e 50 caracteres.");
        if (temporada >= 0)
            this.temporada = (byte) temporada;
        else
            throw new Exception("Temporada inválida! Deve ser maior ou igual a zero.");
        this.dataLancamento = dataLancamento;
        if (duracao >= 0)
            this.duracao = duracao;
        else
            throw new Exception("Duração menor que zero!");    
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }
    public void setDuracao(float duracao) {
        this.duracao = duracao;
    }
    public void setTemporada(int temporada) {
        this.temporada = temporada;
    }
    public int getId() {
        return this.id;
    }
    public String getNome() {
        return this.nome;
    }
    public LocalDate getDataLancamento() {
        return this.dataLancamento;
    }
    public float getDuracao() {
        return this.duracao;
    }
    public int getTemporada() {
        return this.temporada;
    }
    public int getIDSerie() {
        return this.idSerie;
    }   
    
    public String toString() {
        return "\nID.................: " + this.id +
               "\nNome...............: " + this.nome +
               "\nData de lançamento.: " + this.dataLancamento +
               "\nDuração............: " + this.duracao +
               "\nTemporada .........: " + this.temporada +
               "\nAvaliação .........: " + this.avaliacao;
               //para ficar parecido com o do professor
    }

    public float getAvaliacao() {
        return this.avaliacao;
    }
    public void setAvaliacao(float avaliacao) {
        this.avaliacao = avaliacao;
    }
    
    public byte[] toByteArray() throws IOException {//serializa o objeto
        // cria um ByteArrayOutputStream para armazenar os dados serializados
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeInt(idSerie);
        dos.writeUTF(nome);
        dos.writeInt(temporada);
        dos.writeLong(dataLancamento.toEpochDay());
        dos.writeFloat(duracao);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {//deserializa o objeto
        // cria um ByteArrayInputStream para ler os dados serializados
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        idSerie = dis.readInt();
        nome = dis.readUTF();
        temporada = dis.readInt();
        dataLancamento = LocalDate.ofEpochDay(dis.readLong());
        duracao = dis.readInt();
    }
}
