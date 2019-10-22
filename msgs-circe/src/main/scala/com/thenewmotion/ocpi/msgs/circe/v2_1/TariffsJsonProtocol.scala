package com.thenewmotion.ocpi.msgs
package circe.v2_1

import v2_1.Tariffs._
import io.circe.generic.extras.semiauto._
import io.circe.{Decoder, Encoder, HCursor}
import CommonJsonProtocol._
import LocationsJsonProtocol._
import io.circe.Decoder.Result

trait TariffsJsonProtocol {

  implicit val tariffIdE: Encoder[TariffId] = stringEncoder(_.value)
  implicit val tariffIdD: Decoder[TariffId] = tryStringDecoder(TariffId.apply)

  implicit val priceComponentE: Encoder[PriceComponent] = deriveEncoder
  implicit val priceComponentD: Decoder[PriceComponent] = deriveDecoder
  implicit val priceComponentSeqD: Decoder[Seq[PriceComponent]] = decodeSeqTolerantly[PriceComponent]

  implicit val tariffRestrictionsE: Encoder[TariffRestrictions] = deriveEncoder
  implicit val tariffRestrictionsD: Decoder[TariffRestrictions] = deriveDecoder

  implicit val tariffElementE: Encoder[TariffElement] = deriveEncoder

  implicit val tariffElementD: Decoder[TariffElement] = new Decoder[TariffElement] {
    final def apply(c: HCursor): Result[TariffElement] = for {
      pcs <- c.downField("price_components").as[Seq[PriceComponent]]
      restr <- c.downField("restrictions").as[Option[TariffRestrictions]]
    } yield {
      TariffElement(pcs, restr)
    }
  }

  implicit val tariffE: Encoder[Tariff] = deriveEncoder
  implicit val tariffD: Decoder[Tariff] = deriveDecoder
}

object TariffsJsonProtocol extends TariffsJsonProtocol
