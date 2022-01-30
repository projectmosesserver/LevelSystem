package info.ahaha.levelsystem.util;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MaterialToolUtil {

    public MaterialToolUtil(){

    }

    public List<String> getSwords(){
        List<String>list = new ArrayList<>();
        for (Material material : Material.values()){
            if (material.name().contains("SWORD")){
                list.add(material.name());
            }
        }
        return list;
    }

    public List<String> getAxes(){
        List<String>list = new ArrayList<>();
        for (Material material : Material.values()){
            if (material.name().contains("AXE")){
                if (material.name().contains("PICK"))continue;
                if (material.name().contains("WAXED"))continue;
                list.add(material.name());
            }
        }
        return list;
    }

    public List<String> getPickAxes(){
        List<String>list = new ArrayList<>();
        for (Material material : Material.values()){
            if (material.name().contains("PICKAXE")){
                list.add(material.name());
            }
        }
        return list;
    }

    public List<String> getShovels(){
        List<String>list = new ArrayList<>();
        for (Material material : Material.values()){
            if (material.name().contains("SHOVEL")){
                list.add(material.name());
            }
        }
        return list;
    }

    public List<String> getHoes(){
        List<String>list = new ArrayList<>();
        for (Material material : Material.values()){
            if (material.name().contains("HOE")){
                list.add(material.name());
            }
        }
        return list;
    }

}
