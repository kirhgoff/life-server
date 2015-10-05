package org.kirhgoff.ap.core

/**
 * Created by Kirill Lastovirya (kirill.lastovirya@gmail.com) aka kirhgoff on 17/05/15.
 */
trait WorldModelListener {
  def worldUpdated(world:WorldModel):Unit
}
