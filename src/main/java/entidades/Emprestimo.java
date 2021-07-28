package entidades;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Emprestimo {
    private int idEmprestimo;
    private String atividade;
    private int matricula;
    private Timestamp dataSaida;
    private Timestamp dataDevolucao;
    private Timestamp dataEntrega;
    private int renovacoes;
    private boolean penalidade;
    private List<Integer> idsMateriais;

    public Emprestimo(int idEmprestimo, String atividade, int matricula, Timestamp dataSaida, Timestamp dataDevolucao, Timestamp dataEntrega, int renovacoes, boolean penalidade, List<Integer> idsMateriais) {
        this.idEmprestimo = idEmprestimo;
        this.atividade = atividade;
        this.matricula = matricula;
        this.dataSaida = dataSaida;
        this.dataDevolucao = dataDevolucao;
        this.dataEntrega = dataEntrega;
        this.renovacoes = renovacoes;
        this.penalidade = penalidade;
        this.idsMateriais = idsMateriais;
    }

    public Emprestimo(int idEmprestimo, String atividade, int matricula, Timestamp dataSaida, Timestamp dataDevolucao, Timestamp dataEntrega, int renovacoes, boolean penalidade) {
        this.idEmprestimo = idEmprestimo;
        this.atividade = atividade;
        this.matricula = matricula;
        this.dataSaida = dataSaida;
        this.dataDevolucao = dataDevolucao;
        this.dataEntrega = dataEntrega;
        this.renovacoes = renovacoes;
        this.penalidade = penalidade;
        this.idsMateriais = new ArrayList<>();
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

    public Timestamp getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Timestamp dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public int getRenovacoes() {
        return renovacoes;
    }

    public void setRenovacoes(int renovacoes) {
        this.renovacoes = renovacoes;
    }

    public boolean isPenalidade() {
        return penalidade;
    }

    public void setPenalidade(boolean penalidade) {
        this.penalidade = penalidade;
    }

    public List<Integer> getIdsMateriais() {
        return idsMateriais;
    }

    public void setIdsMateriais(List<Integer> idsMateriais) {
        this.idsMateriais = idsMateriais;
    }

    public void adicionaMaterial(int idMaterial) {
        this.idsMateriais.add(idMaterial);
    }

}
