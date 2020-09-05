package com.steventran.fgodb

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.steventran.fgodb.api.DetailedSkill
import java.lang.reflect.Type

private const val TAG = "ServantDeserializer"
class DetailedServantDeserializer: JsonDeserializer<DetailedServant> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): DetailedServant {

        val detailedServantObject = DetailedServant()

        val servantExtraAssets = json?.asJsonObject?.getAsJsonObject("extraAssets")
        val servantCharacterGraphs = servantExtraAssets?.getAsJsonObject("charaGraph")
        val servantAscensions = servantCharacterGraphs?.getAsJsonObject("ascension")
        val costumes = servantCharacterGraphs?.getAsJsonObject("costume")
        val skillsArray = json?.asJsonObject?.get("skills")?.asJsonArray

        Log.d(TAG, "$servantExtraAssets")
        // Code to assign servant image urls to a list
        val servantAscensionGraphUrls = mutableListOf<String>()
        servantAscensions?.keySet()?.forEach {key ->
            Log.d(TAG, "Fetched servantAscension key: $key")
            servantAscensions?.get(key)?.asString?.let {
                // grabs the links of the ascension urls buy obtaining the keys and using them
                servantAscensionGraphUrls.add(
                    it
                )
            }
        }
        Log.d(TAG, "Fetched ascension urls: $servantAscensionGraphUrls.")
        if (costumes?.keySet()?.isNotEmpty() == true) {
            costumes?.keySet()?.forEach { key->
                costumes?.
                get(key)?.
                asString?.let {
                    Log.d(TAG, "Fetched servant costume url: $it")
                    servantAscensionGraphUrls.add(it) }
            }


        }

        // Code to turn skillsArray into DetailedSkill objects
        val skillList: MutableList<DetailedSkill> = mutableListOf()
        skillsArray?.forEach {skillJsonElement ->
            val skill = DetailedSkill()
            val skillCooldownJsonArray = skillJsonElement.asJsonObject
                .getAsJsonArray("coolDown").asIterable()
            val skillFunctionsJsonArray = skillJsonElement.asJsonObject
                .getAsJsonArray("functions")
            val skillFunctionsList = mutableListOf<DetailedSkill.SkillFunction>()
            skillFunctionsJsonArray.forEach {functionJsonElement ->
                // Check to only use resources for functions that are for the player
                if (getStringAttribute(functionJsonElement, "funcTargetTeam") != "enemy" ) {
                    val skillValuesJsonArray = functionJsonElement.asJsonObject
                        .getAsJsonArray("svals")
                    val function = skill.SkillFunction()
                    val skillValues = mutableListOf<DetailedSkill.SkillFunction.SkillValue>()

                    skillValuesJsonArray.forEach { skillValueJsonElement ->
                        val skillValue = function.SkillValue()
                        skillValue.apply {
                            rate = getIntegerAttribute(skillValueJsonElement, "Rate")
                            turnCount = getIntegerAttribute(skillValueJsonElement, "Turn")
                            value = getIntegerAttribute(skillValueJsonElement, "Value")
                        }
                        skillValues.add(skillValue)


                    }
                    function.apply {
                        targetType = getStringAttribute(functionJsonElement, "funcTargetType")
                        //Buff Icon Url isn't always there so will implement later
                        functDescrip = getStringAttribute(functionJsonElement, "funcPopupText")
                        this.skillValues = skillValues
                    }
                    skillFunctionsList.add(function)
                }

            }
            skill.apply {
                name = getStringAttribute(skillJsonElement, "name")
                description = getStringAttribute(skillJsonElement, "detail")
                strengthStatus = getIntegerAttribute(skillJsonElement, "strengthStatus")
                iconUrl = getStringAttribute(skillJsonElement, "icon")
                coolDowns = skillCooldownJsonArray.map { jsonElement ->
                    jsonElement.asInt
                }
                skillFunctions = skillFunctionsList

            }
            skillList.add(skill)

        }

        // Code to assign the variables to DetailedServant
        detailedServantObject.apply {
            collectionNo = getIntegerAttribute(json, "collectionNo")
            servantName = getStringAttribute(json, "name")
            className = getStringAttribute(json, "className")
            type = getStringAttribute(json, "type")
            rarity = getIntegerAttribute(json, "rarity")
            atkBase = getIntegerAttribute(json, "atkBase")
            atkMax = getIntegerAttribute(json, "atkMax")
            hpBase = getIntegerAttribute(json, "hpBase")
            hpMax = getIntegerAttribute(json, "hpMax")
            characterAscensionUrls = servantAscensionGraphUrls
            skills = skillList
        }

        return detailedServantObject

    }
    private fun getStringAttribute(json: JsonElement?, key: String): String{
        return json?.asJsonObject?.get(key)?.asString ?: ""
    }
    private fun getIntegerAttribute(json: JsonElement?, key: String): Int {
        return json?.asJsonObject?.get(key)?.asInt ?: 0
    }
}