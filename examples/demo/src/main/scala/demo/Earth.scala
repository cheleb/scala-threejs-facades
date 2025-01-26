package org.worldofscala.app.world

import org.scalajs.dom.window

//import dev.cheleb.threescalajs.{*, given}

import com.raquo.laminar.api.L.*

//import typings.webxr.*
//import typings.three.mod.*
//import dev.cheleb.ziotapir.laminar.*

import scala.scalajs.js.Math.PI
import typings.three.scenes.Scene
import typings.three.cameras.PerspectiveCamera
import typings.three.renderers.*
import typings.three.examples.controls.OrbitControls
import typings.three.geometries.IcosahedronGeometry
import typings.three.loaders.TextureLoader
import typings.three.materials.MeshBasicMaterial
import typings.three.objects.Group
import typings.three.objects.Mesh
import typings.three.materials.*
import typings.three.objects.Points
import typings.three.renderers.webxr.XRFrameRequestCallback
import typings.three.lights.DirectionalLight
import typings.three.core.BufferGeometry
import typings.three.objects.Line
import typings.three.math.Vector3
import typings.three.math.Color

object Earth {

  val R = 1

  def apply() =

    val eartthDiv = div()

    val scene = new Scene();
    val camera = new PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);

    camera.position.set(0, 0, 5)

    val renderer = new WebGLRenderer(
      new WebGLRendererParameters():
        antialias = true
        alpha = false
    );

    renderer.setPixelRatio(window.devicePixelRatio)
    renderer.setSize(window.innerWidth, window.innerHeight);

    val orbitControl = OrbitControls(camera, renderer.domElement)

    val detail = 300

    val geometry = new IcosahedronGeometry(R - 0.05, 10)
    val pointGeometry = new IcosahedronGeometry(R, detail);

    val textureLoader = TextureLoader()

    val colorMap = textureLoader.load("img/8081-earthmap10k.jpg")

    val material = new MeshBasicMaterial(
      new MeshBasicMaterialParameters():
        color = Color(0xaaaaaa)
        wireframe = false
        alphaToCoverage = true
    );

    val globeGroup = Group()

    val earth = new Mesh(geometry, material);

    globeGroup.add(earth)

    val pointMaterial = PointsMaterial(
      new PointsMaterialParameters():
        color = Color(0xf0f0f0)
        size = 0.02d
        map = colorMap
    )

    val points = Points(pointGeometry, pointMaterial)

    globeGroup.add(points)

    // def addObj(obj: GLTF, location: LatLon) =
    //   val pinner = obj.scene.clone(true)
    //   val (x, y, z) = location.xyz(R + 0.02)
    //   pinner.position.set(x, y, z)
    //   pinner.lookAt(0, 0, 0)
    //   globeGroup.add(pinner)
    //   globeGroup.add(drawLine(x * 1.2, y * 1.2, z * 1.2))

    // val loader = new GLTFLoader()

    // loader.load(
    //   "/public/res/scala.glb",
    //   (obj) => {
    //     addObj(obj, LatLon(46.5188, 6.5593)) // Lauzane
    //   }
    // )

    // eartthDiv.amend(
    //   onMountCallback { _ =>
    //     OrganisationEndpoint
    //       .allStream(())
    //       .jsonl[Organisation, Unit] { organisation =>
    //         val meshIO: ZIO[Any, Any, GLTF] = organisation.meshId match {
    //           case Some(meshId) =>
    //             ZIO.async { callback =>
    //               loader.load(
    //                 SameOriginBackendClientLive.backendBaseURL
    //                   .addPath("api", "mesh", meshId.toString())
    //                   .toString,
    //                 (obj) => {
    //                   callback(ZIO.succeed(obj))
    //                 }
    //               )
    //             }
    //           case None =>
    //             ZIO.async { callback =>
    //               loader.load(
    //                 s"/public/res/pinner.glb",
    //                 (obj) => {
    //                   callback(ZIO.succeed(obj))
    //                 }
    //               )
    //             }
    //         }
    //         (for {
    //           obj <- meshIO
    //           _ <- ZIO.debug(s"Addings ${organisation.name} at ${organisation.location}")
    //           _ <- ZIO.attempt(addObj(obj, organisation.location))
    //         } yield ()).ignore
    //       }
    //   }
    // )

    scene.add(
      globeGroup
    )

    globeGroup.rotation.y = PI / 2
    globeGroup.rotation.x = PI / 4

    val animate: XRFrameRequestCallback = (_, _) => {

      // globeGroup.rotation.x += 0.001;
      globeGroup.rotation.y += 0.005;

      renderer.render(scene, camera);
      // orbitControl.update()

    }
    renderer.setAnimationLoop(animate);

    val light = DirectionalLight(Color(0xffffff), 100)

    light.position.set(5, 5, 5)
    light.lookAt(0, 0, 0)
    scene.add(light)

    eartthDiv.ref.append(renderer.domElement)

    eartthDiv

  def drawLine(
      x: Double,
      y: Double,
      z: Double
  ) = {
    val material = new LineBasicMaterial(
      new LineBasicMaterialParameters():
        color = Color(0x0000ff)
    )
    val geometry = new BufferGeometry().setFromPoints(
      points((0, 0, 0), (x, y, z))
    );
    val line = new Line(geometry, material);
    line
  }
  import scalajs.js.JSConverters.*

  def points(ps: (Double, Double, Double)*) =
    ps.map(p => new Vector3(p._1, p._2, p._3)).toJSArray

}
