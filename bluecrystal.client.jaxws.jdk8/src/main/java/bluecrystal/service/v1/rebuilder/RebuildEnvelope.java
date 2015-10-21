
package bluecrystal.service.v1.rebuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de rebuildEnvelope complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="rebuildEnvelope">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="format" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="envelopeb64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rebuildEnvelope", propOrder = {
    "format",
    "envelopeb64"
})
public class RebuildEnvelope {

    protected int format;
    protected String envelopeb64;

    /**
     * Obtém o valor da propriedade format.
     * 
     */
    public int getFormat() {
        return format;
    }

    /**
     * Define o valor da propriedade format.
     * 
     */
    public void setFormat(int value) {
        this.format = value;
    }

    /**
     * Obtém o valor da propriedade envelopeb64.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvelopeb64() {
        return envelopeb64;
    }

    /**
     * Define o valor da propriedade envelopeb64.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvelopeb64(String value) {
        this.envelopeb64 = value;
    }

}
