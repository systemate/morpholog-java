package ru.systemate.morpholog.client;

import org.s1.S1SystemError;
import org.s1.objects.Objects;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Native client uses reflection
 */
public class NativeMorphologClient extends MorphologClient{
    @Override
    public String declinePhrase(String text, Cases c) {
        if(c==null)
            c = Cases.imenit;
        try{
            Object o = Class.forName("ru.systemate.morpholog.phrase.PhraseDecliner").newInstance();
            for(Method m:o.getClass().getDeclaredMethods()){
                if(m.getName().equals("decline")){

                    return (String)m.invoke(o, text, Enum.valueOf((Class<Enum>)m.getParameterTypes()[1],c.toString()));
                }
            }
        }catch (Throwable e){
            throw S1SystemError.wrap(e);
        }
        return null;
    }

    @Override
    public Genders getGender(String text) {
        try{
            Object o = Class.forName("ru.systemate.morpholog.phrase.PhraseDecliner").newInstance();
            for(Method m:o.getClass().getDeclaredMethods()){
                if(m.getName().equals("getGender")){
                    return Genders.valueOf(m.invoke(o,text).toString());
                }
            }
        }catch (Throwable e){
            throw S1SystemError.wrap(e);
        }
        return null;
    }

    @Override
    public Numeral toNumeral(Number n, Cases c, Genders g, boolean live) {
        if(c==null)
            c = Cases.imenit;
        if(g==null)
            g = Genders.male;
        try{
            Object o = Class.forName("ru.systemate.morpholog.number.ToNumeralConverter").newInstance();
            for(Method m:o.getClass().getDeclaredMethods()){
                if(m.getName().equals("execute")){
                    Object res = m.invoke(o,n,
                            Enum.valueOf((Class<Enum>)m.getParameterTypes()[1],c.toString()),
                            Enum.valueOf((Class<Enum>)m.getParameterTypes()[2],g.toString()),
                            live
                            );
                    String numeral = "";
                    boolean plural = false;
                    Cases labelCase = Cases.imenit;
                    for(Method getter:res.getClass().getDeclaredMethods()){
                        if(getter.getName().equals("getNumeral")){
                            numeral = (String)getter.invoke(res);
                        }else if(getter.getName().equals("getLabelCase")){
                            labelCase = Cases.valueOf(getter.invoke(res).toString());
                        }else if(getter.getName().equals("isPlural")){
                            plural = (Boolean)getter.invoke(res);
                        }
                    }
                    return new Numeral(numeral,labelCase,plural);
                }
            }
        }catch (Throwable e){
            throw S1SystemError.wrap(e);
        }
        return null;
    }

    @Override
    public Numeral formatNumber(Number n, String format) {
        try{
            Object o = Class.forName("ru.systemate.morpholog.number.NumberFormatter").newInstance();
            for(Method m:o.getClass().getDeclaredMethods()){
                if(m.getName().equals("execute")){
                    Object res = m.invoke(o,n,format);
                    String numeral = "";
                    boolean plural = false;
                    Cases labelCase = Cases.imenit;
                    for(Method getter:res.getClass().getDeclaredMethods()){
                        if(getter.getName().equals("getNumeral")){
                            numeral = (String)getter.invoke(res);
                        }else if(getter.getName().equals("getLabelCase")){
                            labelCase = Cases.valueOf(getter.invoke(res).toString());
                        }else if(getter.getName().equals("isPlural")){
                            plural = (Boolean)getter.invoke(res);
                        }
                    }
                    return new Numeral(numeral,labelCase,plural);
                }
            }
        }catch (Throwable e){
            throw S1SystemError.wrap(e);
        }
        return null;
    }

    @Override
    public String formatTimeDiff(long diff, boolean round, boolean toNumeral) {
        try{
            Object o = Class.forName("ru.systemate.morpholog.number.TimeDiffFormatter").newInstance();
            for(Method m:o.getClass().getDeclaredMethods()){
                if(m.getName().equals("execute")){
                    return (String)m.invoke(o,diff,round,toNumeral);
                }
            }
        }catch (Throwable e){
            throw S1SystemError.wrap(e);
        }
        return null;
    }

    @Override
    public List<FWord> detectFWords(String text) {
        try{
            Object o = Class.forName("ru.systemate.morpholog.fword.FWordDetector").newInstance();
            for(Method m:o.getClass().getDeclaredMethods()){
                if(m.getName().equals("execute")){
                    List res = (List)m.invoke(o,text);
                    List<FWord> result = Objects.newArrayList();
                    for(Object r:res){
                        String word = "";
                        FWordDictionaries type = null;
                        long start = 0;
                        int length = 0;
                        for(Method getter:r.getClass().getDeclaredMethods()){
                            if(getter.getName().equals("getFword")){
                                word = (String)getter.invoke(r);
                            }else if(getter.getName().equals("getType")){
                                type = FWordDictionaries.valueOf(getter.invoke(r).toString());
                            }else if(getter.getName().equals("getStart")){
                                start = (Long)getter.invoke(r);
                            }else if(getter.getName().equals("getLength")){
                                length = Objects.cast(getter.invoke(r),Integer.class);
                            }
                        }
                        result.add(new FWord(word,start,length,type));
                    }
                    return result;
                }
            }
        }catch (Throwable e){
            throw S1SystemError.wrap(e);
        }
        return null;
    }
}
