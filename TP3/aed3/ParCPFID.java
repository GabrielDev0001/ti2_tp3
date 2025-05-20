package aed3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParCPFID implements aed3.RegistroHashExtensivel<ParCPFID> {
    
    private String nome; // chave
    private int id;     // valor
    private final short TAMANHO = 15;  // tamanho em bytes

    public ParCPFID() {
        this.nome = "";
        this.id = -1;
    }

    public ParCPFID(String cpf, int id) throws Exception {
        // Certifique-se de que o CPF contém exatamente 11 dígitos
        this.nome = cpf;
        this.id = id;
    }

    public String getnome() {
        return nome;
    }

    public int getId() {
        return id;
    }

 
    @Override
    public int hashCode() {
        return hash(this.nome);
    }

    public short size() {
        return this.TAMANHO;
    }

    public String toString() {
        return "("+this.nome + ";" + this.id+")";
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(this.nome);
        dos.writeInt(this.id);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.nome = dis.readUTF();
        this.id = dis.readInt();
    }

    public static int hash(String nome) throws IllegalArgumentException {
        // Certifique-se de que o CPF contém exatamente 11 dígitos
        // Converter o CPF para um número inteiro longo
        long cpfLong = (long) nome.hashCode();

        // Aplicar uma função de hash usando um número primo grande
        int hashValue = (int) (cpfLong % (int)(1e9 + 7));

        // Retornar um valor positivo
        return Math.abs(hashValue);
    }


}
