package com.lilithsthrone.game.character.npc.submission;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.lilithsthrone.game.character.CharacterImportSetting;
import com.lilithsthrone.game.character.CharacterUtils;
import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.character.attributes.Attribute;
import com.lilithsthrone.game.character.body.valueEnums.AssSize;
import com.lilithsthrone.game.character.body.valueEnums.BodySize;
import com.lilithsthrone.game.character.body.valueEnums.CupSize;
import com.lilithsthrone.game.character.body.valueEnums.Femininity;
import com.lilithsthrone.game.character.body.valueEnums.HairLength;
import com.lilithsthrone.game.character.body.valueEnums.HipSize;
import com.lilithsthrone.game.character.body.valueEnums.LipSize;
import com.lilithsthrone.game.character.body.valueEnums.Muscle;
import com.lilithsthrone.game.character.fetishes.Fetish;
import com.lilithsthrone.game.character.gender.Gender;
import com.lilithsthrone.game.character.npc.NPC;
import com.lilithsthrone.game.character.persona.Name;
import com.lilithsthrone.game.character.persona.SexualOrientation;
import com.lilithsthrone.game.character.race.RaceStage;
import com.lilithsthrone.game.character.race.Subspecies;
import com.lilithsthrone.game.combat.Attack;
import com.lilithsthrone.game.dialogue.DialogueNodeOld;
import com.lilithsthrone.game.dialogue.npcDialogue.submission.TunnelImpsDialogue;
import com.lilithsthrone.game.dialogue.responses.Response;
import com.lilithsthrone.game.dialogue.utils.UtilText;
import com.lilithsthrone.game.inventory.CharacterInventory;
import com.lilithsthrone.game.inventory.clothing.AbstractClothing;
import com.lilithsthrone.game.inventory.clothing.AbstractClothingType;
import com.lilithsthrone.game.inventory.clothing.ClothingType;
import com.lilithsthrone.game.inventory.enchanting.EnchantingUtils;
import com.lilithsthrone.game.inventory.enchanting.ItemEffect;
import com.lilithsthrone.game.inventory.enchanting.TFModifier;
import com.lilithsthrone.game.inventory.enchanting.TFPotency;
import com.lilithsthrone.game.inventory.item.AbstractItem;
import com.lilithsthrone.game.inventory.item.AbstractItemType;
import com.lilithsthrone.game.inventory.item.ItemType;
import com.lilithsthrone.game.inventory.weapon.AbstractWeaponType;
import com.lilithsthrone.game.inventory.weapon.WeaponType;
import com.lilithsthrone.game.settings.ForcedTFTendency;
import com.lilithsthrone.main.Main;
import com.lilithsthrone.utils.Util;
import com.lilithsthrone.utils.Util.Value;
import com.lilithsthrone.utils.Vector2i;
import com.lilithsthrone.world.WorldType;
import com.lilithsthrone.world.places.PlaceType;

/**
 * @since 0.2.11
 * @version 0.2.11
 * @author Innoxia
 */
public class ImpAttacker extends NPC {

	public ImpAttacker() {
		this(Subspecies.IMP, Gender.F_V_B_FEMALE, false);
	}
	
	public ImpAttacker(boolean isImported) {
		this(Subspecies.IMP, Gender.F_V_B_FEMALE, isImported);
	}
	
	public ImpAttacker(Subspecies subspecies, Gender gender) {
		this(subspecies, gender, false);
	}
	
	public ImpAttacker(Subspecies subspecies, Gender gender, boolean isImported) {
		super(isImported, null, "",
				Util.random.nextInt(28)+18, Util.randomItemFrom(Month.values()), 1+Util.random.nextInt(25),
				3, gender, subspecies, RaceStage.GREATER,
				new CharacterInventory(10), WorldType.SUBMISSION, PlaceType.SUBMISSION_TUNNELS, false);

		if(!isImported) {
			this.setWorldLocation(Main.game.getPlayer().getWorldLocation());
			this.setLocation(new Vector2i(Main.game.getPlayer().getLocation().getX(), Main.game.getPlayer().getLocation().getY()));
			
			// Set random level from 5 to 8:
			setLevel(5 + Util.random.nextInt(4));
			
			setSexualOrientation(SexualOrientation.AMBIPHILIC);
	
			setName(Name.getRandomTriplet(this.getRace()));
			this.setPlayerKnowsName(false);
			setDescription(UtilText.parse(this,
					"Imps, such as this one, have no interest in anything but sex, and will attack anyone who's not a member of their clan in order to get what they want..."));
			
			// PERSONALITY & BACKGROUND:
			
			CharacterUtils.setHistoryAndPersonality(this, true);
			
			// ADDING FETISHES:
			
			CharacterUtils.addFetishes(this);
			
			
			// BODY RANDOMISATION:
			
			CharacterUtils.randomiseBody(this, true);
			
			// INVENTORY:
			
			resetInventory(true);
			inventory.setMoney(10 + Util.random.nextInt(getLevel()*10) + 1);
			CharacterUtils.generateItemsInInventory(this);
	
			// Clothing is equipped in the Encounter class, when the imps are spawned.
			CharacterUtils.applyMakeup(this, true);
			
			// Set starting attributes based on the character's race
			initAttributes();
			
			setMana(getAttributeValue(Attribute.MANA_MAXIMUM));
			setHealth(getAttributeValue(Attribute.HEALTH_MAXIMUM));
		}
		
		this.setEnslavementDialogue(TunnelImpsDialogue.IMP_ENSLAVEMENT_DIALOGUE);
	}
	
	@Override
	public void loadFromXML(Element parentElement, Document doc, CharacterImportSetting... settings) {
		loadNPCVariablesFromXML(this, null, parentElement, doc, settings);
	}

	@Override
	public void setStartingBody(boolean setPersona) {
		// Not needed
	}

	@Override
	public void equipClothing(boolean replaceUnsuitableClothing, boolean addWeapons, boolean addScarsAndTattoos, boolean addAccessories) { //TODO gang tattoos?
		CharacterUtils.equipPiercings(this, true);
		
		if(!this.getAllSpells().isEmpty()) {
			if(addWeapons) {
				this.equipMainWeaponFromNowhere(AbstractWeaponType.generateWeapon(WeaponType.getWeaponTypeFromId("innoxia_arcanistStaff_arcanist_staff")));
			}
			this.equipClothingFromNowhere(AbstractClothingType.generateClothing(ClothingType.getClothingTypeFromId("innoxia_impArcanist_arcanist_hat"), false), true, this);
		}
		
		if(this.isFeminine()) {
			AbstractClothing skirt = AbstractClothingType.generateClothing(ClothingType.getClothingTypeFromId("innoxia_loinCloth_ragged_skirt"), false);
			this.equipClothingFromNowhere(skirt, true, this);
			
			// Imps are flying, and don't wear anything on their feet.
			// Alpha-thiss also wear accessories as symbols of status.
			if(this.getSubspecies()==Subspecies.IMP_ALPHA) {
				this.equipClothingFromNowhere(AbstractClothingType.generateClothing(ClothingType.getClothingTypeFromId("innoxia_loinCloth_foot_wraps"), false), true, this);
				this.equipClothingFromNowhere(AbstractClothingType.generateClothing(ClothingType.WRIST_BANGLE, false), true, this);
				this.equipClothingFromNowhere(AbstractClothingType.generateClothing(ClothingType.FINGER_RING, false), true, this);
				this.equipClothingFromNowhere(AbstractClothingType.generateClothing(Util.randomItemFrom(new AbstractClothingType[] {ClothingType.NECK_ANKH_NECKLACE, ClothingType.NECK_HEART_NECKLACE}), false), true, this);
			}
			
			if(!this.hasFetish(Fetish.FETISH_EXHIBITIONIST)) {
				List<AbstractClothingType> underwear = Util.newArrayListOfValues(
						ClothingType.GROIN_THONG,
						ClothingType.GROIN_VSTRING,
						ClothingType.GROIN_PANTIES,
						ClothingType.GROIN_CROTCHLESS_THONG,
						ClothingType.GROIN_CROTCHLESS_PANTIES);
				this.equipClothingFromNowhere(
						AbstractClothingType.generateClothing(Util.randomItemFrom(underwear), false), true, this);

				this.equipClothingFromNowhere(AbstractClothingType.generateClothing(ClothingType.getClothingTypeFromId("innoxia_loinCloth_ragged_chest_wrap"), skirt.getColour(), false), true, this);
			}
			
		} else {
			this.equipClothingFromNowhere(
					AbstractClothingType.generateClothing(ClothingType.getClothingTypeFromId("innoxia_loinCloth_loin_cloth"), false), true, this);

			// Imps are flying, and don't wear anything on their feet.
			// Alpha-thiss also wear accessories as symbols of status.
			if(this.getSubspecies()==Subspecies.IMP_ALPHA) {
				this.equipClothingFromNowhere(AbstractClothingType.generateClothing(ClothingType.getClothingTypeFromId("innoxia_loinCloth_foot_wraps"), false), true, this);
				this.equipClothingFromNowhere(AbstractClothingType.generateClothing(ClothingType.HAND_WRAPS, false), true, this);
				this.equipClothingFromNowhere(AbstractClothingType.generateClothing(ClothingType.STOMACH_SARASHI, false), true, this);
			}
		}
	
	}
	
	@Override
	public boolean isUnique() {
		return false;
	}
	
	@Override
	public String getDescription() {
		if(this.isSlave()) {
			return (UtilText.parse(this,
					"[npc.NamePos] days of prowling the tunnels of Submission and assaulting innocent travellers are now over. Having run afoul of the law, [npc.sheIs] now a slave, and is no more than [npc.her] owner's property."));
		} else {
			return (UtilText.parse(this, description));
		}
	}
	
	@Override
	public void endSex() {
		if(!isSlave()) {
			setPendingClothingDressing(true);
		}
	}

	@Override
	public boolean isClothingStealable() {
		return true;
	}
	
	@Override
	public boolean isAbleToBeImpregnated() {
		return true;
	}
	
	@Override
	public void changeFurryLevel(){
	}
	
	@Override
	public DialogueNodeOld getEncounterDialogue() {
		return TunnelImpsDialogue.IMP_ATTACK;
	}

	// Combat:
	
	public Attack attackType() {
		
		// If can cast spells, then do that:
		if(!getSpellsAbleToCast().isEmpty()) {
			return Attack.SPELL;
		}

		// If female without weapon, seduce:
		if(this.getMainWeapon()==null && this.isFeminine()) {
			return Attack.SEDUCTION;
		}

		Map<Attack, Integer> attackWeightingMap = new HashMap<>();
		boolean canCastASpecialAttack = !getSpecialAttacksAbleToUse().isEmpty();

		attackWeightingMap.put(Attack.MAIN, this.getRace().getPreferredAttacks().contains(Attack.MAIN)?75:50);
		attackWeightingMap.put(Attack.OFFHAND, this.getOffhandWeapon()==null?0:(this.getRace().getPreferredAttacks().contains(Attack.MAIN)?50:25));
		attackWeightingMap.put(Attack.SPECIAL_ATTACK, !canCastASpecialAttack?0:(this.getRace().getPreferredAttacks().contains(Attack.MAIN)?100:50));
		
		int total = 0;
		for(Entry<Attack, Integer> entry : attackWeightingMap.entrySet()) {
			total+=entry.getValue();
		}
		
		int index = Util.random.nextInt(total);
		total = 0;
		for(Entry<Attack, Integer> entry : attackWeightingMap.entrySet()) {
			total+=entry.getValue();
			if(index<total) {
				return entry.getKey();
			}
		}
		
		return Attack.MAIN;
	}
	
	@Override
	public void applyEscapeCombatEffects() {
		TunnelImpsDialogue.banishImpGroup();
	};
	
	@Override
	public Response endCombat(boolean applyEffects, boolean victory) {
		if (victory) {
			Value<String, AbstractItem> potion = TunnelImpsDialogue.getImpLeader().getTransfomativePotion(Main.game.getPlayer(), true);
			TunnelImpsDialogue.getImpGroup().get(1).addItem(potion.getValue(), false);
			if(!Main.game.getPlayer().getNonElementalCompanions().isEmpty()) {
				Value<String, AbstractItem> potion2 = TunnelImpsDialogue.getImpLeader().getTransfomativePotion(Main.game.getPlayer().getMainCompanion(), true);
				TunnelImpsDialogue.getImpGroup().get(1).addItem(potion2.getValue(), false);
			}
			
			return new Response("", "", TunnelImpsDialogue.AFTER_COMBAT_VICTORY);
			
		} else {
			return new Response("", "", TunnelImpsDialogue.AFTER_COMBAT_DEFEAT);
		}
	}
	
	// TF potion:

	@Override
	public Value<String, AbstractItem> getTransfomativePotion(GameCharacter target, boolean generateNew) {
		if(generateNew) {
			this.heldTransformativePotion = generateTransformativePotion(target);
		}
		
		return this.heldTransformativePotion;
	}
	
	@Override
	public Value<String, AbstractItem> generateTransformativePotion(GameCharacter target) {
		
		AbstractItemType itemType = ItemType.RACE_INGREDIENT_HUMAN;
		switch(target.getRace()) {
			case ALLIGATOR_MORPH:
				itemType = ItemType.RACE_INGREDIENT_ALLIGATOR_MORPH;
				break;
			case BAT_MORPH:
				itemType = ItemType.RACE_INGREDIENT_BAT_MORPH;
				break;
			case CAT_MORPH:
				itemType = ItemType.RACE_INGREDIENT_CAT_MORPH;
				break;
			case COW_MORPH:
				itemType = ItemType.RACE_INGREDIENT_COW_MORPH;
				break;
			case DEMON: //TODO Will need changing later on:
				itemType = ItemType.RACE_INGREDIENT_DEMON;
				break;
			case DOG_MORPH:
				itemType = ItemType.RACE_INGREDIENT_DOG_MORPH;
				break;
			case FOX_MORPH:
				itemType = ItemType.RACE_INGREDIENT_FOX_MORPH;
				break;
			case HARPY:
				itemType = ItemType.RACE_INGREDIENT_HARPY;
				break;
			case HORSE_MORPH:
				itemType = ItemType.RACE_INGREDIENT_HORSE_MORPH;
				break;
			case RABBIT_MORPH:
				itemType = ItemType.RACE_INGREDIENT_RABBIT_MORPH;
				break;
			case RAT_MORPH:
				itemType = ItemType.RACE_INGREDIENT_RAT_MORPH;
				break;
			case REINDEER_MORPH:
				itemType = ItemType.RACE_INGREDIENT_REINDEER_MORPH;
				break;
			case SQUIRREL_MORPH:
				itemType = ItemType.RACE_INGREDIENT_SQUIRREL_MORPH;
				break;
			case WOLF_MORPH:
				itemType = ItemType.RACE_INGREDIENT_WOLF_MORPH;
				break;

			case ANGEL:
			case HUMAN:
			case NONE:
			case SLIME:
			case ELEMENTAL_AIR:
			case ELEMENTAL_ARCANE:
			case ELEMENTAL_EARTH:
			case ELEMENTAL_FIRE:
			case ELEMENTAL_WATER:
				break;
		}
		
		List<ItemEffect> effects = new ArrayList<>();

		
		/* TODO
		 * Alpha:
		 * Give penis and vagina and increase cum and wetness. If TF setting is feminine, TF to female.
		 * 
		 * Demon:
		 * Most powerful transformation
		 * Transforms into female or male (based on player preferences).
		 * Gives kinks so want to fuck or get fucked. (based on player preferences)
		 * 
		 * Females:
		 * TF setting neutral: Give large penis & cum, and long tongue.
		 * TF setting masculine: Turn into full male/herm.
		 * 
		 * Males:
		 * TF setting neutral: Give wet vagina.
		 * TF setting feminine: Turn into full female/futa.
		 * 
		 */
		
		if(target.getLocationPlace().getPlaceType()==PlaceType.SUBMISSION_IMP_TUNNELS_ALPHA) {
			
			if(Main.getProperties().getForcedTFTendency()==ForcedTFTendency.FEMININE
					|| Main.getProperties().getForcedTFTendency()==ForcedTFTendency.FEMININE_HEAVY) {
				effects.addAll(getFeminineEffects(target, itemType));
			}
			
			// Add wet vagina:
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.NONE, TFPotency.MINOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1));
			
			// Add penis:
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.NONE, TFPotency.MINOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_WETNESS, TFPotency.MINOR_BOOST, 1));
			
		} else if(target.getLocationPlace().getPlaceType()==PlaceType.SUBMISSION_IMP_TUNNELS_DEMON) {

			if(Main.getProperties().getForcedTFTendency()==ForcedTFTendency.MASCULINE
					|| Main.getProperties().getForcedTFTendency()==ForcedTFTendency.MASCULINE_HEAVY) {
				effects.addAll(getMasculineEffects(target, itemType));
				
			} else {
				effects.addAll(getFeminineEffects(target, itemType));
			}

			// Add penis:
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.NONE, TFPotency.MINOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.MAJOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_WETNESS, TFPotency.MINOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_WETNESS, TFPotency.MINOR_BOOST, 1));
			
			// Add wet vagina:
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.NONE, TFPotency.MINOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1));
			
		} else if(target.getLocationPlace().getPlaceType()==PlaceType.SUBMISSION_IMP_TUNNELS_FEMALES) {
			
			if(Main.getProperties().getForcedTFTendency()==ForcedTFTendency.MASCULINE
					|| Main.getProperties().getForcedTFTendency()==ForcedTFTendency.MASCULINE_HEAVY) {
				effects.addAll(getMasculineEffects(target, itemType));
			}
			
			// Add penis:
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.NONE, TFPotency.MINOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE, TFPotency.BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.BOOST, 1));

			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE_TERTIARY, TFPotency.MINOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE_TERTIARY, TFPotency.MINOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE_TERTIARY, TFPotency.MINOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1));
			
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE, TFPotency.BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.BOOST, 1));
			
			// Add long tongue (for cunnilingus):
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_FACE, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.BOOST, 1));
			
			
		} else { // SUBMISSION_IMP_TUNNELS_MALES

			if(Main.getProperties().getForcedTFTendency()==ForcedTFTendency.FEMININE
					|| Main.getProperties().getForcedTFTendency()==ForcedTFTendency.FEMININE_HEAVY) {
				effects.addAll(getFeminineEffects(target, itemType));
			}
			
			// Add wet vagina:
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.NONE, TFPotency.MINOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1));
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1));
			
		}
		
		return new Value<>(
				"",
				EnchantingUtils.craftItem(AbstractItemType.generateItem(itemType), effects));
	}
	
	private static List<ItemEffect> getMasculineEffects(GameCharacter target, AbstractItemType itemType) {
		List<ItemEffect>effects = new ArrayList<>();
		
		for(int i=target.getFemininityValue(); i>Femininity.MASCULINE.getMinimumFemininity(); i-=15) { // Turn masculine:
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_FEMININITY, TFPotency.MAJOR_DRAIN, 1));
		}
		if(target.getMuscleValue()<Muscle.THREE_MUSCULAR.getMaximumValue()) {
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.MAJOR_BOOST, 1));
		}
		if(target.getBodySizeValue()<BodySize.TWO_AVERAGE.getMinimumValue()) {
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_SIZE_TERTIARY, TFPotency.MAJOR_BOOST, 1));
		}
		if(target.getHeightValue()<183) { // 6'
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_BOOST, 1));
		}
		for(int i=target.getBreastSize().getMeasurement(); i>0; i-=3) {
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_BREASTS, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_DRAIN, 1));
		}
		if(target.getHipSize().getValue()>HipSize.TWO_NARROW.getValue()) {
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ASS, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.MAJOR_DRAIN, 1));
		}
		if(target.getAssSize().getValue()>AssSize.THREE_NORMAL.getValue()) {
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ASS, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_DRAIN, 1));
		}
		if(target.getHairRawLengthValue()>0) { // If bald, leave bald.
			for(int i=target.getHairRawLengthValue(); i>HairLength.TWO_SHORT.getMaximumValue(); i-=15) {
				effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_HAIR, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_DRAIN, 1));
			}
		}
		for(int i=target.getLipSizeValue(); i>LipSize.TWO_FULL.getValue(); i-=2) {
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_FACE, TFModifier.TF_MOD_SIZE, TFPotency.DRAIN, 1));
		}
		
		return effects;
	}
	
	private static List<ItemEffect> getFeminineEffects(GameCharacter target, AbstractItemType itemType) {
		List<ItemEffect>effects = new ArrayList<>();
		
		for(int i=target.getFemininityValue(); i<Femininity.FEMININE.getMinimumFemininity(); i+=15) { // Turn feminine:
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_FEMININITY, TFPotency.MAJOR_BOOST, 1));
		}
		if(target.getMuscleValue()>Muscle.THREE_MUSCULAR.getMedianValue()) {
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.MAJOR_DRAIN, 1));
		}
		if(target.getBodySizeValue()>BodySize.TWO_AVERAGE.getMinimumValue()) {
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_SIZE_TERTIARY, TFPotency.MAJOR_DRAIN, 1));
		}
		if(target.getHeightValue()>167) { // 5'6"
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_DRAIN, 1));
		}
		for(int i=target.getBreastSize().getMeasurement(); i<CupSize.E.getMeasurement(); i+=3) {
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_BREASTS, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_BOOST, 1));
		}
		if(target.getHipSize().getValue()<HipSize.THREE_GIRLY.getValue()) {
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ASS, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.MAJOR_BOOST, 1));
		}
		if(target.getAssSize().getValue()<AssSize.THREE_NORMAL.getValue()) {
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ASS, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_BOOST, 1));
		}
		if(target.getHairRawLengthValue()>0) { // If bald, leave bald.
			for(int i=target.getHairRawLengthValue(); i<HairLength.THREE_SHOULDER_LENGTH.getMaximumValue(); i+=15) {
				effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_HAIR, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_BOOST, 1));
			}
		}
		for(int i=target.getLipSizeValue(); i<LipSize.TWO_FULL.getValue(); i+=2) {
			effects.add(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_FACE, TFModifier.TF_MOD_SIZE, TFPotency.BOOST, 1));
		}
		
		return effects;
	}
}
