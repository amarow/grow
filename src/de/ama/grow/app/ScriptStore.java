package de.ama.grow.app;

import de.ama.grow.script.Sequence;
import de.ama.grow.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class ScriptStore {


    private static ScriptStore instance=null;
    private Map<String,Sequence> sequences = new HashMap<>();
    private String errorText=null;


    public static void load(String txt){
        get().setScriptText(txt);
    }

    public static ScriptStore get(){
        if(instance==null){
            instance = new ScriptStore();
        }
        return instance;
    }


    public void setScriptText(String scriptText) {
        try {
            read(scriptText);
        } catch (IOException e) {
            errorText = e.getMessage();
        }
    }

    private void read(String scriptText) throws IOException {

        if(Util.isEmpty(scriptText)){
            errorText = "no scriptText to read detected";
            return;
        }

        BufferedReader br = new BufferedReader(new StringReader(scriptText));

        sequences.clear();

        try {
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line.trim());
            }
        } finally {
            if(br!=null)
                br.close();
        }
        if(getSequence()==null){
            errorText = "no Sequence to run detected";
        }

    }

    private void parseLine(String line) {
        if(line.isEmpty()) return;
        if(line.startsWith("#")) return;
        if(line.startsWith("//")) return;

        String key = null;
        String value = null;

        if(line.contains(":")){
            String[] split = line.split(":");
            key = split[0].trim();
            value = split[1].trim();
        } else {
            value = line;
        }


        if(key==null || key.isEmpty()){
            key = "run";
        }

        try {

        if(key.equalsIgnoreCase("maxcells")){
                Environment.get().setMaxCells(Integer.parseInt(value));
        } else if(key.equalsIgnoreCase("pause")){
                Environment.get().setPause(Integer.parseInt(value));
        } else if(key.equalsIgnoreCase("cellsize")){
                Environment.get().setCellSize(Double.parseDouble(value));
        } else if(key.equalsIgnoreCase("shape")){
                Environment.get().setShapeBox("box".equalsIgnoreCase(value));
        } else {
            Sequence duplicate = sequences.put(key, Sequence.createSequences(value));
            if(duplicate!=null && !key.equals("run")){
                errorText = "duplicate Sequence "+key+" is not allowed";
            }
        }
    } catch (NumberFormatException e) {
        errorText = e.getMessage();
    }

    }

    public Sequence getSequence(){
        return sequences.get("run");
    }

    public String getErrorText() {
        return errorText;
    }

    public Sequence getSequence(String key) {
        return sequences.get(key).clone();
    }

  }
