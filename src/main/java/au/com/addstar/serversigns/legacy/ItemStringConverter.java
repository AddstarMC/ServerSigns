package au.com.addstar.serversigns.legacy;

import au.com.addstar.serversigns.utils.MaterialConvertor;
import au.com.addstar.serversigns.utils.NumberUtils;
import org.bukkit.Material;

public class ItemStringConverter {
    public static String convertPreV4String(String input) {
        if ((input.contains(" ")) && (!input.split(" ")[0].contains("."))) {
            String newInput = "";
            String[] split = input.split(" ");
            if (split.length >= 3) {
                for (int k = 0; k < split.length; k++) {
                    switch (k) {
                        case 0:
                            Material mat = MaterialConvertor.getMaterialById(NumberUtils.parseInt(split[k], -1));
                            if (mat != null) {
                                newInput = newInput + "id." + mat.toString() + " ";
                            } else {
                                return input;
                            }

                            break;
                        case 1:
                            newInput = newInput + "am." + split[k] + " ";
                            break;
                        case 2:
                            newInput = newInput + "du." + split[k] + " ";
                            break;
                        case 3:
                            if (!split[k].startsWith("name")) {
                                newInput = newInput + "na." + split[k] + " ";
                            } else {
                                newInput = newInput + "na." + split[k].substring(5) + " ";
                            }
                            break;
                        case 4:
                            if (!split[k].equals("#91643")) {
                                if (!split[k].startsWith("lore")) {
                                    newInput = newInput + "lo." + split[k] + " ";
                                } else {
                                    newInput = newInput + "lo." + split[k].substring(5) + " ";
                                }
                            }
                            break;
                        case 5:
                            if (!split[k].startsWith("lore")) {
                                newInput = newInput + "lo." + split[k] + " ";
                            } else {
                                newInput = newInput + "lo." + split[k].substring(5) + " ";
                            }
                            break;
                        default:
                            if ((split[k].startsWith("name:")) || (split[k].startsWith("name."))) {
                                newInput = newInput + "na." + split[k].substring(5) + " ";
                            } else if ((split[k].startsWith("lore:")) || (split[k].startsWith("lore."))) {
                                newInput = newInput + "lo." + split[k].substring(5) + " ";
                            } else {
                                newInput = newInput + split[k] + " ";
                            }
                            break;
                    }
                }
                input = newInput;
            }
        }

        return input.trim();
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\legacy\ItemStringConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */