using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class UpdateWorld : MonoBehaviour {

	private static string url = "http://localhost:8080/world";
	private int worldVersion = 0;
	private World world = null;

	// Objects in the world
	public Dictionary<string, GameObject> entities = new Dictionary<string, GameObject>(); // list of created GameObjects

	// prefabs to create objects
	public GameObject kartPrefab;

	void Start () {
		Application.runInBackground = true;
	}

	void Update () {
		StartCoroutine (CacheWorld ());

		if (this.world != null) {
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
			Debug.LogWarning ("Error requesting " + url + ": " + worldRequest.error);
		}
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
		// TODO figure out which way we want to use X and Y
		gameObject.transform.localScale = new Vector3 (entity.width, 1, entity.height);
		gameObject.transform.position = new Vector3 (entity.centerX, 0, entity.centerY);
		gameObject.transform.rotation = Quaternion.AngleAxis (entity.rotationDegs, new Vector3 (0, -1, 0));
	}

}
