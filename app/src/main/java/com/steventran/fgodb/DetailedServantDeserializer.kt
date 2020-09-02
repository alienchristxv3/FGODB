package com.steventran.fgodb

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.steventran.fgodb.api.DetailedSkill
import java.lang.reflect.Type

class DetailedServantDeserializer: JsonDeserializer<DetailedServant> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): DetailedServant {
        val detailedServantObject = DetailedServant()

        val servantExtraAssets = json?.asJsonObject?.getAsJsonObject("extraAssets")
        val servantCharacterGraphs = servantExtraAssets?.getAsJsonObject("charaGraph")
        val servantAscensions = servantCharacterGraphs?.getAsJsonObject("ascensions")
        val costumes = servantCharacterGraphs?.getAsJsonObject("costume")
        val skillsArray = json?.asJsonObject?.get("skills")?.asJsonArray


        // Code to assign servant image urls to a list
        val servantAscensionGraphUrls = mutableListOf<String>()
        servantAscensions?.keySet()?.forEach {key ->
            servantAscensions?.get(key)?.asString?.let {
                // grabs the links of the ascension urls buy obtaining the keys and using them
                servantAscensionGraphUrls.add(
                    it
                )
            }
        }
        if (costumes?.keySet()?.isNotEmpty()!!) {
            costumes?.keySet().forEach { key->
                servantAscensionGraphUrls.add(costumes.getAsJsonObject(key).asString)
            }
        }

        // Code to turn skillsArray into DetailedSkill objects
        val skillList: MutableList<DetailedSkill> = mutableListOf()
        skillsArray?.forEach {skillJsonElement ->
            val skill = DetailedSkill()
            val skillCooldownJsonArray = skillJsonElement.asJsonObject
                .getAsJsonArray("coolDown").asIterable() as List<JsonElement>
            skill.apply {
                name = getStringAttribute(skillJsonElement, "name")
                description = getStringAttribute(skillJsonElement, "detail")
                strengthStatus = getIntegerAttribute(skillJsonElement, "strengthStatus")
                iconUrl = getStringAttribute(skillJsonElement, "icon")
                coolDowns = skillCooldownJsonArray.map { jsonElement ->
                    jsonElement.asInt
                }
                
            }

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

        }

        return detailedServantObject

    }
    private fun getStringAttribute(json: JsonElement, key: String): String{
        return json.asJsonObject.get(key).asString
    }
    private fun getIntegerAttribute(json: JsonElement, key: String): Int {
        return json.asJsonObject.get(key).asInt
    }
}