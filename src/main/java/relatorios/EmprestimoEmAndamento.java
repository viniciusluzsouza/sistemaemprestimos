package relatorios;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoEmAndamento {
    private int idEmprestimo;
    private String atividade;
    private int matricula;
    private Timestamp dataSaida;
    private Timestamp dataDevolucao;
    private int renovacoes;
    private List<String> materiais;
    private String nome;
    private String sobrenome;

    public EmprestimoEmAndamento(int idEmprestimo, String atividade, int matricula, Timestamp dataSaida, Timestamp dataDevolucao, int renovacoes, String nome, String sobrenome) {
        this.idEmprestimo = idEmprestimo;
        this.atividade = atividade;
        this.matricula = matricula;
        this.dataSaida = dataSaida;
        this.dataDevolucao = dataDevolucao;
        this.renovacoes = renovacoes;
        this.materiais = new ArrayList<>();
        this.nome = nome;
        this.sobrenome = sobrenome;
    }

    public void adicionaMaterial(String material) {
        this.materiais.add(material);
    }

    public int getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(int idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public Timestamp getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Timestamp dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Timestamp getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Timestamp dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public int getRenovacoes() {
        return renovacoes;
    }

    public void setRenovacoes(int renovacoes) {
        this.renovacoes = renovacoes;
    }

    public List<String> getMateriais() {
        return materiais;
    }

    public void setMateriais(List<String> materiais) {
        this.materiais = materiais;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }
}
