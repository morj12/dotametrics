package com.example.dotametrics.presentation.view.hero

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.constants.abilities.AbilityResult
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.util.UrlConstants

@Composable
fun HeroScreen(
    heroState: HeroResult,
    viewModel: HeroViewModel,
    constViewModel: ConstViewModel
) {
    val heroAbilitiesMap by constViewModel.constHeroAbilities.observeAsState(emptyMap())
    val allAbilities by constViewModel.constAbilities.observeAsState(emptyMap())
    val aghsList by constViewModel.constAghs.observeAsState(emptyList())
    LocalContext.current

    LaunchedEffect(heroState) {
        viewModel.setHero(heroState)
        constViewModel.loadHeroAbilities()
        constViewModel.loadAghs()
    }

    LaunchedEffect(heroAbilitiesMap) {
        if (heroAbilitiesMap.isNotEmpty()) {
            constViewModel.loadAbilities()
        }
    }

    val isLoaded = heroAbilitiesMap.containsKey(heroState.name) && allAbilities.isNotEmpty()
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.purple_900))) {
        if (!isLoaded) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                HeroHeader(heroState)

                // Abilities
                Text(
                    text = "Abilities",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                heroAbilitiesMap[heroState.name]?.abilities?.forEach { abilityName ->
                    allAbilities[abilityName]?.let { ability ->
                        if (!ability.behavior.any { it.contains("Hidden", ignoreCase = true) }) {
                            AbilityItem(abilityName, ability)
                        }
                    }
                }

                // Talents
                val heroTalentsInfo = heroAbilitiesMap[heroState.name]?.talents
                if (!heroTalentsInfo.isNullOrEmpty()) {
                    Text(
                        text = "Talents",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            val levels = listOf(25, 20, 15, 10)
                            levels.forEachIndexed { index, level ->
                                val currentLevelTalents = heroTalentsInfo.filter { it.level == (4 - index) }
                                if (currentLevelTalents.size >= 2) {
                                    val talentA = allAbilities[currentLevelTalents[0].name]?.name ?: ""
                                    val talentB = allAbilities[currentLevelTalents[1].name]?.name ?: ""
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = talentA, modifier = Modifier.weight(1f), color = Color.White, fontSize = 11.sp, textAlign = TextAlign.End)
                                        Text(
                                            text = level.toString(),
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            color = Color.Yellow,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                        Text(text = talentB, modifier = Modifier.weight(1f), color = Color.White, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Aghanim Info
                val aghsInfo = aghsList.firstOrNull { it.heroId == heroState.id }
                if (aghsInfo != null) {
                    Text(
                        text = "Upgrades",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    AghsShardItem(
                        icon = R.drawable.scepter,
                        skillName = aghsInfo.scepterSkillName ?: "",
                        desc = aghsInfo.scepterDescription ?: ""
                    )
                    
                    AghsShardItem(
                        icon = R.drawable.shard,
                        skillName = aghsInfo.shardSkillName ?: "",
                        desc = aghsInfo.shardDescription ?: ""
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HeroHeader(heroState: HeroResult) {
    val context = LocalContext.current
    val headerBrush = Brush.verticalGradient(
        colors = listOf(
            colorResource(id = R.color.purple_700),
            colorResource(id = R.color.purple_900)
        )
    )

    Box(modifier = Modifier.fillMaxWidth().background(headerBrush)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val heroName = heroState.name?.replace(UrlConstants.HEROES_URL_REPLACE, "")
                    AsyncImage(
                        model = "${UrlConstants.HEROES_URL}/${heroName}.png",
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp, 90.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = heroState.localizedName ?: "",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (heroState.primaryAttr) {
                                            "str" -> colorResource(id = R.color.red)
                                            "agi" -> colorResource(id = R.color.green)
                                            "int" -> colorResource(id = R.color.blue)
                                            else -> Color.Yellow
                                        }
                                    )
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = mapAttr(context, heroState.primaryAttr),
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Section with better design
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    // Attributes
                    Column(modifier = Modifier.weight(1.1f)) {
                        AttributeStatRow(
                            icon = R.drawable.attr,
                            value = "${heroState.baseStr?.toInt() ?: 0}",
                            gain = "+${heroState.strGain ?: 0.0}",
                            color = colorResource(id = R.color.red)
                        )
                        AttributeStatRow(
                            icon = R.drawable.attr,
                            value = "${heroState.baseAgi?.toInt() ?: 0}",
                            gain = "+${heroState.agiGain ?: 0.0}",
                            color = colorResource(id = R.color.green)
                        )
                        AttributeStatRow(
                            icon = R.drawable.attr,
                            value = "${heroState.baseInt?.toInt() ?: 0}",
                            gain = "+${heroState.intGain ?: 0.0}",
                            color = colorResource(id = R.color.blue)
                        )
                    }
                    
                    Spacer(Modifier.width(16.dp))

                    // Secondary Stats
                    Column(modifier = Modifier.weight(0.9f)) {
                        SecondaryStatRow(label = "SPEED", value = heroState.moveSpeed?.toString() ?: "0")
                        SecondaryStatRow(label = "RESIST", value = "${heroState.basicMagicResist?.toInt() ?: 0}%")
                        SecondaryStatRow(label = "RANGE", value = heroState.attackRange?.toString() ?: "0")
                        SecondaryStatRow(label = "BAT", value = heroState.baseAttackTime?.toString() ?: "0.0")
                    }
                }
            }
        }
    }
}

private fun mapAttr(context: Context, attr: String?): String {
    return when (attr) {
        "str" -> context.getString(R.string.str_name)
        "agi" -> context.getString(R.string.agi_name)
        "int" -> context.getString(R.string.int_name)
        "all" -> context.getString(R.string.all_name)
        else -> "Unknown"
    }
}

@Composable
fun AttributeStatRow(icon: Int, value: String, gain: String, color: Color) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(8.dp))
        Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(4.dp))
        Text(text = gain, color = color, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SecondaryStatRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AbilityItem(abilityName: String, ability: AbilityResult) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row {
                val imageUrl = "${UrlConstants.ABILITIES_URL}/${abilityName}.png"
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(text = ability.name ?: "", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = ability.description ?: "", color = Color.LightGray, fontSize = 12.sp, lineHeight = 16.sp)
                }
            }

            if (ability.attributes.isNotEmpty() || ability.coolDown.isNotEmpty() || ability.manaCost.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(8.dp))
                
                ability.attributes.forEach { attr ->
                    Text(
                        text = "${attr.header} ${attr.value.joinToString(", ")}",
                        color = Color.White,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }

                Row(modifier = Modifier.padding(top = 8.dp)) {
                    if (ability.coolDown.isNotEmpty()) {
                        Text(
                            text = "CD: ${ability.coolDown.joinToString("/")}",
                            color = Color.Gray,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                    if (ability.manaCost.isNotEmpty()) {
                        Text(
                            text = "MC: ${ability.manaCost.joinToString("/")}",
                            color = colorResource(id = R.color.blue),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AghsShardItem(icon: Int, skillName: String, desc: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = skillName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = desc, color = Color.LightGray, fontSize = 12.sp, lineHeight = 16.sp)
            }
        }
    }
}

@Preview
@Composable
fun HeroHeaderPreview() {
    MaterialTheme {
        HeroHeader(
            heroState = HeroResult(
                id = 1,
                name = "npc_dota_hero_antimage",
                localizedName = "Anti-Mage",
                primaryAttr = "agi",
                baseStr = 21.0,
                strGain = 1.6,
                baseAgi = 24.0,
                agiGain = 2.8,
                baseInt = 12.0,
                intGain = 1.8,
                baseAttackTime = 1.4,
                moveSpeed = 310,
                attackRange = 150,
                basicMagicResist = 25.0
            )
        )
    }
}
