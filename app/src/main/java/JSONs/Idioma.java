package JSONs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Idioma implements Serializable {
    public List<Pregunta> infantil;
    public List<Pregunta> facil;
    public List<Pregunta> medio;
    public List<Pregunta> dificil;

    public Idioma()
    {
        infantil = new ArrayList<Pregunta>();
        facil = new ArrayList<Pregunta>();
        medio = new ArrayList<Pregunta>();
        dificil = new ArrayList<Pregunta>();
    }

    public List<Pregunta> getInfantil() {
        return infantil;
    }

    public void setInfantil(List<Pregunta> infantil) {
        this.infantil = infantil;
    }

    public List<Pregunta> getFacil() {
        return facil;
    }

    public void setFacil(List<Pregunta> facil) {
        this.facil = facil;
    }

    public List<Pregunta> getMedio() {
        return medio;
    }

    public void setMedio(List<Pregunta> medio) {
        this.medio = medio;
    }

    public List<Pregunta> getDificil() {
        return dificil;
    }

    public void setDificil(List<Pregunta> dificil) {
        this.dificil = dificil;
    }
}
