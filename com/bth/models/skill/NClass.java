package com.bth.models.skill;

import com.bth.models.Template;
import com.bth.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Heroes x BTH
 * 
 */
public class NClass {

    public int classId;

    public String name;
    
    public List<Template.SkillTemplate> skillTemplatess = new ArrayList<>();
    
    public Template.SkillTemplate getSkillTemplate(int tempId){
        for (Template.SkillTemplate skillTemplate : skillTemplatess) {
            if (skillTemplate.id == tempId){
                return skillTemplate;
            }
        }
        return null;
    }
    
    public Template.SkillTemplate getSkillTemplateByName(String name){
        for (Template.SkillTemplate skillTemplate : skillTemplatess) {
            if((Util.removeAccent(skillTemplate.name).toUpperCase()).contains((Util.removeAccent(name)).toUpperCase())){
                return skillTemplate;
           }
        }
        return null;
    }
    
}
