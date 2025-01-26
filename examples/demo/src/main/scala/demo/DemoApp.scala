package demo

import com.raquo.laminar.api.L.*
import org.scalajs.dom
import org.scalajs.dom.window

import org.worldofscala.app.world.Earth
import typings.three.scenes.Scene
import typings.three.cameras.PerspectiveCamera
import typings.three.renderers.WebGLRenderer
import typings.three.geometries.BoxGeometry
import typings.three.materials.MeshBasicMaterial
import typings.three.objects.Mesh
import typings.three.math.Color
import typings.three.renderers.webxr.XRFrameRequestCallback

object DemoApp extends App {
  val myApp = div(
    h1("Hello, Laminar!"),
    button("Click me"),
    HelloWorld()
  )

  val containerNode = dom.document.getElementById("app")
  render(containerNode, Earth())
}

object HelloWorld:
  def apply() =
    val aDiv = div()
    val scene = new Scene();
    val camera = new PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);

    val renderer = new WebGLRenderer();
    renderer.setSize(window.innerWidth, window.innerHeight);

    val animate: XRFrameRequestCallback = (_, _) => {
      renderer.render(scene, camera);
    }

    renderer.setAnimationLoop(animate)

    val cubeGeometry = BoxGeometry(1, 1, 1);
    val cubeMaterial = new MeshBasicMaterial():
      color = Color(0x00ff00);
    val cube = new Mesh(cubeGeometry, cubeMaterial);
    scene.add(cube);

    camera.position.z = 5;

    aDiv.ref.append(renderer.domElement)

    aDiv
