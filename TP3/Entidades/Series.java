package Entidades;
import aed3.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class Series implements Registro {

    public int id;
    public String nome;
    public String sinopse;
    public LocalDate lancamento;
    public String stream;

    public Series() {
        this(-1, "", "", LocalDate.now(),"");
    }
    public Series(String n, String c, LocalDate d, String s) {
        this(-1, n, c, d, s);
    }

    public Series(int i, String n, String c, LocalDate d, String s) {
        this.id = i;
        this.nome = n;
        this.sinopse = c;
        this.lancamento = d;
        this. stream = s;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setStream(String stream) {
        this.stream = stream;
    }
    public String getStream() {
        return stream;
    }
    public void setLancamento(LocalDate lancamento) {
        this.lancamento = lancamento;
    }
    public LocalDate getLancamento() {
        return lancamento;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }
    public String getSinopse() {
        return sinopse;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }



    public String toString() {
        return "\nID........: " + this.id +
               "\nNome......: " + this.nome +
               "\nSinopse.......: " + this.sinopse +
               "\nStream...: " + this.stream +
               "\nAno de Lançamento: " + this.lancamento;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.sinopse);
        dos.writeUTF(this.stream);
        dos.writeInt((int) this.lancamento.toEpochDay());
        return baos.toByteArray();
    }


    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.sinopse = dis.readUTF();
        this.stream = dis.readUTF();
        this.lancamento = LocalDate.ofEpochDay(dis.readInt());
    }

}
