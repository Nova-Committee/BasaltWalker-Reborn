package committee.nova.migration.bswk.common.block.init

import committee.nova.migration.bswk.common.block.impl.BlockSoftBasalt
import cpw.mods.fml.common.registry.GameRegistry

object BlockInit {
  val softBasalt = new BlockSoftBasalt

  def init(): Unit = GameRegistry.registerBlock(softBasalt, "soft_basalt")
}
