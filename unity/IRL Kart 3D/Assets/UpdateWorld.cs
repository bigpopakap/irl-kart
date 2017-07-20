using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class UpdateWorld : MonoBehaviour {

	private static bool SHOULD_DRAW_PHYSICAL_ENTITIES = true;

	private static string url = "http://localhost:8080/world";
	private int worldVersion = 0;
	private World world = null;

	// Objects in the world
	public Dictionary<string, GameObject> entities = new Dictionary<string, GameObject>(); // list of created GameObjects

	// prefabs to create objects
	public GameObject kartPrefab;
	public GameObject shellPrefab;
	public GameObject bananaPrefab;

	void Start () {
		Application.runInBackground = true;
	}

	void Update () {
		StartCoroutine (CacheWorld ());

		if (this.world != null) {
			PruneRemovedEntities (this.entities, this.world, this.worldVersion);
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

	private void PruneRemovedEntities(Dictionary<string, GameObject> entities, World world, int worldVersion) {
		ArrayList toRemove = new ArrayList ();

		foreach (string id in entities.Keys) {
			bool found = false;

			foreach (WorldEntity worldEntity in world.entities) {
				if (id.Equals (worldEntity.id)) {
					found = true;
					break;
				}
			}

			if (!found) {
				toRemove.Add (id);
			}
		}

		foreach (string id in toRemove.ToArray()) {
			Destroy (entities [id]);
			entities.Remove (id);
		}
	}

	private void CreateNewEntities(Dictionary<string, GameObject> entities, World world, int worldVersion) {
		foreach (WorldEntity worldEntity in world.entities) {
			if (!SHOULD_DRAW_PHYSICAL_ENTITIES && !worldEntity.isVirtual) {
				// do nothing. We won't draw this
			}

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
		if (entity.type.Equals ("Kart")) {
			GameObject kart = Instantiate (this.kartPrefab);
			kart.transform.localScale = new Vector3 (entity.lengthX, 10, entity.lengthZ); // TODO don't hardcode the car height
			return kart;
		} else if (entity.type.Equals ("Shell")) {
			GameObject shell = Instantiate (this.shellPrefab);
			shell.transform.localScale = new Vector3 (100, 100, 100); // TODO don't hardcode this size
			return shell;
		} else if (entity.type.Equals ("Banana")) {
			GameObject banana = Instantiate (this.bananaPrefab);
			return banana;
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
		// TODO update the localScale here instead of when the object is created
		gameObject.transform.position = new Vector3 (entity.centerX, 0, entity.centerZ);
		gameObject.transform.rotation = Quaternion.AngleAxis (entity.rotationDegs, new Vector3 (0, -1, 0));
	}

}
