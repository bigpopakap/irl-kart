using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class UpdateWorld : MonoBehaviour {

	private static string url = "http://localhost:8080/world";
	private World world;

	void Update () {
		StartCoroutine (CacheWorld ());
		print (world.dimensions.minX);
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
			yield return world;
		} else {
			print ("Error requesting " + url + ": " + worldRequest.error);
		}
	}

}
