package JSONs;

public class Resultado {
    private String nombre;
    private int puntación;
    private int insignias;

    public Resultado() {
    }

    public Resultado(String nombre, int puntación, int insignias) {
        this.nombre = nombre;
        this.puntación = puntación;
        this.insignias = insignias;
    }

    public int getInsignias() {
        return insignias;
    }

    public void setInsignias(int insignias) {
        this.insignias = insignias;
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
