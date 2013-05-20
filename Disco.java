import java.util.ArrayList;
// Imports para lectura facil de XML
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class Disco {
  int cilindros;
  int capacidadDisco;
  int capacidadPlatos;
  int tamanoSector;
  int densidad;
  ArrayList<Cabezal> cabezales;
  int rpm;
  int tfreno, tarranque;
  int tasaLectura, tasaEscritura;
  ArrayList<Peticion> peticiones;

  public static void main(String[] args) {
    (new Disco()).leerPeticiones("petitions.xml");
  }

  public Disco() {

  }

  /*
   * Modifica la variable 'peticiones'
   */
  public void leerPeticiones(String xml) {

    try {
      File file = new File(xml);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(file);
      NodeList petitions, blocks;
      Element petE, blE;
      Node pet, bl;

      peticiones = new ArrayList<Peticion>();
      Peticion in;
      String prioridad, tipo;
      int tiempo, bloque;

      doc.getDocumentElement().normalize();

      petitions = doc.getElementsByTagName("petition");

      for (int i = 0; i < petitions.getLength(); i++) {

        pet = petitions.item(i);
        if (pet.getNodeType() == Node.ELEMENT_NODE) {

          petE = (Element) pet;

          prioridad = petE.getElementsByTagName("priority")
                      .item(0).getTextContent();
          tiempo    = Integer.parseInt(petE.getElementsByTagName("time")
                                        .item(0).getTextContent());
          tipo      = petE.getElementsByTagName("type")
                      .item(0).getTextContent();

          in = new Peticion(tiempo, tipo, prioridad);

          blocks = petE.getElementsByTagName("block");

          for (int j = 0; j < blocks.getLength(); j++) {

            bl = blocks.item(j);
            bloque = Integer.parseInt(bl.getTextContent());

            in.addBloque(bloque);
          }

          peticiones.add(in);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
