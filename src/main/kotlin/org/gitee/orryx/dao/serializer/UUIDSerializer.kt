package org.gitee.orryx.dao.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.gitee.orryx.utils.parseUUID
import java.util.*

object UUIDSerializer : KSerializer<UUID> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("orryx.UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        val string = value.toString()
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): UUID {
        return decoder.decodeString().parseUUID()!!
    }
}