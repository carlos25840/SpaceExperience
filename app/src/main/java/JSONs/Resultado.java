package JSONs;

public class Resultado {
    private String nombre;
    private int puntación;

    public Resultado(String nombre, int puntación) {
        this.nombre = nombre;
        this.puntación = puntación;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntación() {
        return puntación;
    }

    public void setPuntación(int puntación) {
        this.puntación = puntación;
    }
}
