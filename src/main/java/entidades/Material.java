package entidades;

import java.util.ArrayList;
import java.util.List;

public class Material {
    private int idMaterial;
    private String nomeMaterial;
    private String tipoMaterial;
    private int reserva;
    private boolean temEmprestimo;
    private List<Componente> componentes;

    public Material(int idMaterial, String nomeMaterial, String tipoMaterial, int reserva, boolean temEmprestimo, List<Componente> componentes) {
        this.idMaterial = idMaterial;
        this.nomeMaterial = nomeMaterial;
        this.tipoMaterial = tipoMaterial;
        this.reserva = reserva;
        this.temEmprestimo = temEmprestimo;
        this.componentes = componentes;
    }

    public Material(int idMaterial, String nomeMaterial, String tipoMaterial, int reserva, boolean temEmprestimo) {
        this.idMaterial = idMaterial;
        this.nomeMaterial = nomeMaterial;
        this.tipoMaterial = tipoMaterial;
        this.reserva = reserva;
        this.temEmprestimo = temEmprestimo;
        this.componentes = new ArrayList<>();
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public String getNomeMaterial() {
        return nomeMaterial;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public int getReserva() {
        return reserva;
    }

    public boolean getReservaBoolean() {
        return reserva > 0;
    }

    public boolean isTemEmprestimo() {
        return temEmprestimo;
    }

    public void setTemEmprestimo(boolean temEmprestimo) {
        this.temEmprestimo = temEmprestimo;
    }

    public List<Componente> getComponentes() {
        return componentes;
    }

    public void setComponentes(List<Componente> componentes) {
        this.componentes = componentes;
    }
}
