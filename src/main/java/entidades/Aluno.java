package entidades;

public class Aluno {
    private int matricula;
    private String nome;
    private String sobrenome;
    private boolean ativo;
    private int curso;

    public Aluno(int matricula, String nome, String sobrenome, int curso, boolean ativo) {
        this.matricula = matricula;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.curso = curso;
        this.ativo = ativo;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }
}
