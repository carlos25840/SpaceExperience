package JSONs;

public class Resultado implements Comparable {
    private String nombre;
    private int puntacion;
    private int insignias;

    public Resultado() {
    }

    public Resultado(String nombre, int puntacion, int insignias) {
        this.nombre = nombre;
        this.puntacion = puntacion;
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

    public int getPuntacion() {
        return puntacion;
    }

    public void setPuntacion(int puntacion) {
        this.puntacion = puntacion;
    }

    @Override
    public int compareTo(Object o) {
        int comparePunt=((Resultado)o).getPuntacion();
        return comparePunt-this.puntacion;
    }
}
