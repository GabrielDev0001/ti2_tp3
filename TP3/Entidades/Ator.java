package Entidades;

import aed3.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Ator implements Registro {

    public int id;
    public String nome;

    public Ator (String nome){
        this.nome = nome;
        this.id = -1;
    } 

    public Ator (int id, String nome, int idserie){
        this.id = id;
        this.nome = nome;
    }

    public Ator () {
        this.id = -1;
        this.nome = "";
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getId() {
        return this.id;
    }
    public String getNome() {
        return this.nome;
    }
    public byte[] toByteArray() throws IOException {//serializa o objeto
        // cria um ByteArrayOutputStream para armazenar os dados serializados
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
    
       
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {//deserializa o objeto
        // cria um ByteArrayInputStream para ler os dados serializados
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.nome = dis.readUTF();
    }
}