package ru.systemate.morpholog.client;

import org.s1.S1SystemError;
import org.s1.format.xml.XMLFormat;
import org.s1.misc.IOUtils;
import org.s1.objects.Objects;
import org.s1.script.S1ScriptEngine;
import org.s1.ws.SOAPHelper;
import org.w3c.dom.Element;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.util.List;
import java.util.Map;

/**
 * Web service client
 */
public class WSMorphologClient extends MorphologClient{

    private static final String ENDPOINT = "http://service.systemate.ru/morpholog/dispatcher/MorphologWS";

    private SOAPMessage createMessage(String action, Map<String,Object> params){
        params.put("action",action);
        S1ScriptEngine scriptEngine = new S1ScriptEngine();
        String s = IOUtils.toString(this.getClass().getResourceAsStream(action+".xml"),"UTF-8");
        s = scriptEngine.template(s,params,"${","}","<%","%>");
        return SOAPHelper.createSoapFromString(s);
    }

    private SOAPMessage send(SOAPMessage msg){
        SOAPMessage r = null;
        try {
            r = SOAPHelper.send(ENDPOINT, msg);
        } catch (SOAPException e) {
            throw S1SystemError.wrap(e);
        }
        return r;
    }

    @Override
    public String declinePhrase(String text, Cases c) {
        if(c==null)
            c = Cases.imenit;
        String action = "DeclinePhrase";
        SOAPMessage msg = createMessage(action, Objects.newHashMap(String.class,Object.class,
                "text",text,
                "wordCase",c.toString()));
        msg = send(msg);
        String res = XMLFormat.get(SOAPHelper.getEnvelope(msg),"Body."+action+"Response.Text",null);
        return res;
    }

    @Override
    public Genders getGender(String text) {
        String action = "GetGender";
        SOAPMessage msg = createMessage(action, Objects.newHashMap(String.class,Object.class,
                "text",text));
        msg = send(msg);
        String res = XMLFormat.get(SOAPHelper.getEnvelope(msg),"Body."+action+"Response.Text",null);
        return Genders.valueOf(res);
    }

    @Override
    public Numeral toNumeral(Number n, Cases c, Genders g, boolean live) {
        if(c==null)
            c = Cases.imenit;
        if(g==null)
            g = Genders.male;
        String action = "ToNumeral";
        SOAPMessage msg = createMessage(action, Objects.newHashMap(String.class, Object.class,
                "num", n,
                "wordCase", c.toString(),
                "gender", g.toString(),
                "live", live));
        msg = send(msg);
        String t = XMLFormat.get(SOAPHelper.getEnvelope(msg),"Body."+action+"Response.Text",null);
        Cases lc = Cases.valueOf(XMLFormat.get(SOAPHelper.getEnvelope(msg),"Body."+action+"Response.Case",null));
        boolean pl = Objects.cast(XMLFormat.get(SOAPHelper.getEnvelope(msg),"Body."+action+"Response.Plural",null),Boolean.class);
        return new Numeral(t,lc,pl);
    }

    @Override
    public Numeral formatNumber(Number n, String format) {
        String action = "FormatNumber";
        SOAPMessage msg = createMessage(action, Objects.newHashMap(String.class, Object.class,
                "num", n,
                "format", format));
        msg = send(msg);
        String t = XMLFormat.get(SOAPHelper.getEnvelope(msg),"Body."+action+"Response.Text",null);
        Cases lc = Cases.valueOf(XMLFormat.get(SOAPHelper.getEnvelope(msg),"Body."+action+"Response.Case",null));
        boolean pl = Objects.cast(XMLFormat.get(SOAPHelper.getEnvelope(msg),"Body."+action+"Response.Plural",null),Boolean.class);
        return new Numeral(t,lc,pl);
    }

    @Override
    public String formatTimeDiff(long diff, boolean round, boolean toNumeral) {
        String action = "FormatTimeDiff";
        SOAPMessage msg = createMessage(action, Objects.newHashMap(String.class, Object.class,
                "diff", diff,
                "round", round,
                "toNumeral", toNumeral));
        msg = send(msg);
        String t = XMLFormat.get(SOAPHelper.getEnvelope(msg), "Body." + action + "Response.Text", null);
        return t;
    }

    @Override
    public List<FWord> detectFWords(String text) {
        String action = "DetectFWords";
        SOAPMessage msg = createMessage(action, Objects.newHashMap(String.class, Object.class,
                "text", text));
        msg = send(msg);
        List<FWord> result = Objects.newArrayList();
        Element resp = XMLFormat.getElement(SOAPHelper.getEnvelope(msg), "Body." + action + "Response", null);
        for(Element fw:XMLFormat.getChildElementList(resp,"FWord",null)){
            String w = XMLFormat.get(fw,"Word",null);
            FWordDictionaries t = FWordDictionaries.valueOf(XMLFormat.get(fw,"Type",null));
            long start = Objects.cast(XMLFormat.get(fw, "Start", null), Long.class);
            int length = Objects.cast(XMLFormat.get(fw, "Length", null), Integer.class);
            result.add(new FWord(w,start,length,t));
        }
        return result;
    }
}
