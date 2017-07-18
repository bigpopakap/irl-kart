using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class WorldEntity {
	public string id;
	public string type;
	public bool isVirtual;
	public float lengthX;
	public float lengthZ;
	public float centerX;
	public float centerZ;
	public float rotationDegs;
}
