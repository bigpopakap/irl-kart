using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class UpdateWorld : MonoBehaviour {

	private static string url = "http://localhost:8080/world";
	private int worldVersion = 0;
	private World world = null;

	// objects in the Unity world
	public GameObject floor; // an actual GameObject, not a pre-fab
	public Dictionary<string, GameObject> entities = new Dictionary<string, GameObject>(); // list of created GameObjects

	// prefabs to create objects
	public GameObject kartPrefab;

	void Update () {
		StartCoroutine (CacheWorld ());

		if (this.world != null) {
			UpdateFloor (this.floor, this.world, this.worldVersion);
			CreateNewEntities (this.entities, this.world, this.worldVersion);
			UpdateExistingEntities (this.entities, this.world, this.worldVersion);
		}
	}
		
	private IEnumerator CacheWorld() {
		WWW worldRequest = new WWW (url);
		while (!worldRequest.isDone) {
			// do nothing
		}
		if (worldRequest.error == null || worldRequest.error.Equals("")) {
			string worldJsonText = worldRequest.text;
			World world = JsonUtility.FromJson<World> (worldJsonText);
			this.world = world;

			this.worldVersion++;
			print("World version " + this.worldVersion + ") received");

			yield return world;
		} else {
			print ("Error requesting " + url + ": " + worldRequest.error);
		}
	}

	private void UpdateFloor(GameObject floor, World world, int worldVersion) {
		print ("World version " + worldVersion + ") transforming floor to " + floor.transform.localScale);
		floor.transform.localScale = new Vector3 (world.dimensions.width, 1, world.dimensions.height);
	}

	private void CreateNewEntities(Dictionary<string, GameObject> entities, World world, int worldVersion) {
		foreach (WorldEntity worldEntity in world.entities) {
			if (!entities.ContainsKey (worldEntity.id)) {
				print ("World version " + worldVersion + ") trying to create new entity of type " + worldEntity.type + " with ID " + worldEntity.id + "...");

				GameObject newEntity = CreateNewEntity (worldEntity);
				if (newEntity != null) {
					entities [worldEntity.id] = newEntity;
				}
			}
		}
	}

	private GameObject CreateNewEntity(WorldEntity entity) {
		if (entity.type.Equals ("class irl.kart.entities.Kart")) {
			return Instantiate (this.kartPrefab);
		} else {
			Debug.LogWarning ("Unexpected entity type: " + entity.type);
			return null;
		}
	}

	private void UpdateExistingEntities(Dictionary<string, GameObject> entities, World world, int worldVersion) {
		foreach (WorldEntity worldEntity in world.entities) {
			if (entities.ContainsKey (worldEntity.id)) {
				print ("World version " + worldVersion + ") updating entity of type " + worldEntity.type + " with ID " + worldEntity.id + "...");

				GameObject gameObject = entities [worldEntity.id];
				UpdateExistingEntity (gameObject, worldEntity);
			}
		}
	}

	private void UpdateExistingEntity(GameObject gameObject, WorldEntity entity) {
		// TODO
	}

}
