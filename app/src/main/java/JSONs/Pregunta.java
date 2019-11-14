package JSONs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pregunta implements Serializable {
    public String pregunta;
    public List<Respuesta> respuestas;

    //Constructores
    public Pregunta()
    {
    }
    public Pregunta(String pregunta, List<Respuesta> respuestas)
    {
        this.respuestas = new ArrayList<Respuesta>();
        this.pregunta = pregunta;

        for (Respuesta r : respuestas)
        {
            this.respuestas.add(r);
        }
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}
