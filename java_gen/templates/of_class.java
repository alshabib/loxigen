//:: # Copyright 2013, Big Switch Networks, Inc.
//:: #
//:: # LoxiGen is licensed under the Eclipse Public License, version 1.0 (EPL), with
//:: # the following special exception:
//:: #
//:: # LOXI Exception
//:: #
//:: # As a special exception to the terms of the EPL, you may distribute libraries
//:: # generated by LoxiGen (LoxiGen Libraries) under the terms of your choice, provided
//:: # that copyright and licensing notices generated by LoxiGen are not altered or removed
//:: # from the LoxiGen Libraries and the notice provided below is (i) included in
//:: # the LoxiGen Libraries, if distributed in source code form and (ii) included in any
//:: # documentation for the LoxiGen Libraries, if distributed in binary form.
//:: #
//:: # Notice: "Copyright 2013, Big Switch Networks, Inc. This library was generated by the LoxiGen Compiler."
//:: #
//:: # You may not use this file except in compliance with the EPL or LOXI Exception. You may obtain
//:: # a copy of the EPL at:
//:: #
//:: # http::: #www.eclipse.org/legal/epl-v10.html
//:: #
//:: # Unless required by applicable law or agreed to in writing, software
//:: # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
//:: # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
//:: # EPL for the specific language governing permissions and limitations
//:: # under the EPL.
//::
//:: from loxi_ir import *
//:: import os
//:: import itertools
//:: import of_g
//:: include('_copyright.java')

//:: include('_autogen.java')

package ${msg.package};

//:: include("_imports.java", msg=msg)

class ${impl_class} implements ${msg.interface.inherited_declaration()} {
    // version: ${version}
    final static byte WIRE_VERSION = ${version.int_version};
//:: if msg.is_fixed_length:
    final static int LENGTH = ${msg.length};
//:: else:
    final static int MINIMUM_LENGTH = ${msg.min_length};
//:: #endif

//:: for prop in msg.data_members:
    //:: if prop.default_value:
        private final static ${prop.java_type.public_type} ${prop.default_name} = ${prop.default_value};
    //:: #endif
//:: #end

    // OF message fields
//:: for prop in msg.data_members:
    private final ${prop.java_type.public_type} ${prop.name};
//:: #endfor

    //:: if msg.data_members:
    // package private constructor - used by readers, builders, and factory
    ${impl_class}(${
        ", ".join("%s %s" %(prop.java_type.public_type, prop.name) for prop in msg.data_members) }) {
//:: for prop in msg.data_members:
        this.${prop.name} = ${prop.name};
//:: #endfor
    }
    //:: else:
    final static ${impl_class} INSTANCE = new ${impl_class}();
    // private empty constructor - use shared instance!
    private ${impl_class}() {
    }
    //:: #endif

    // Accessors for OF message fields
    //:: include("_field_accessors.java", msg=msg, generate_setters=False, builder=False, has_parent=False)

    //:: if os.path.exists("%s/custom/%s.java" % (template_dir, msg.name)):
    //:: include("custom/%s.java" % msg.name, msg=msg)
    //:: #endif

    //:: if msg.data_members:
    public ${msg.interface.name}.Builder createBuilder() {
        return new BuilderWithParent(this);
    }

    static class BuilderWithParent implements ${msg.interface.name}.Builder {
        final ${impl_class} parentMessage;

        // OF message fields
//:: for prop in msg.data_members:
        private boolean ${prop.name}Set;
        private ${prop.java_type.public_type} ${prop.name};
//:: #endfor

        BuilderWithParent(${impl_class} parentMessage) {
            this.parentMessage = parentMessage;
        }

//:: include("_field_accessors.java", msg=msg, generate_setters=True, builder=True, has_parent=True)


        @Override
        public ${msg.interface.name} build() {
                //:: for prop in msg.data_members:
                ${prop.java_type.public_type} ${prop.name} = this.${prop.name}Set ? this.${prop.name} : parentMessage.${prop.name};
                //::    if not prop.is_nullable and not prop.java_type.is_primitive:
                if(${prop.name} == null)
                    throw new NullPointerException("Property ${prop.name} must not be null");
                //::    #endif
                //:: #endfor

                //
                //:: if os.path.exists("%s/custom/%s.Builder_normalize_stanza.java" % (template_dir, msg.name)):
                //:: include("custom/%s.Builder_normalize_stanza.java" % msg.name, msg=msg, has_parent=False)
                //:: #endif
                return new ${impl_class}(
                //:: for i, prop in enumerate(msg.data_members):
                //::    comma = "," if i < len(msg.data_members)-1 else ""
                    ${prop.name}${comma}
                //:: #endfor
                );
        }
        //:: if os.path.exists("%s/custom/%s.Builder.java" % (template_dir, msg.name)):
        //:: include("custom/%s.Builder.java" % msg.name, msg=msg, has_parent=True)
        //:: #endif

    }

    static class Builder implements ${msg.interface.name}.Builder {
        // OF message fields
//:: for prop in msg.data_members:
        private boolean ${prop.name}Set;
        private ${prop.java_type.public_type} ${prop.name};
//:: #endfor

//:: include("_field_accessors.java", msg=msg, generate_setters=True, builder=True, has_parent=False)
//
        @Override
        public ${msg.interface.name} build() {
            //:: for prop in msg.data_members:
            //::    if prop.default_value:
            ${prop.java_type.public_type} ${prop.name} = this.${prop.name}Set ? this.${prop.name} : ${prop.default_name};
            //:: else:
            if(!this.${prop.name}Set)
                throw new IllegalStateException("Property ${prop.name} doesn't have default value -- must be set");
            //::    #endif
            //::    if not prop.is_nullable and not prop.java_type.is_primitive:
            if(${prop.name} == null)
                throw new NullPointerException("Property ${prop.name} must not be null");
            //::    #endif
            //:: #endfor

            //:: if os.path.exists("%s/custom/%s.Builder_normalize_stanza.java" % (template_dir, msg.name)):
            //:: include("custom/%s.Builder_normalize_stanza.java" % msg.name, msg=msg, has_parent=False)
            //:: #endif

            return new ${impl_class}(
                //:: for i, prop in enumerate(msg.data_members):
                //::    comma = "," if i < len(msg.data_members)-1 else ""
                    ${prop.name}${comma}
                //:: #endfor
                );
        }
        //:: if os.path.exists("%s/custom/%s.Builder.java" % (template_dir, msg.name)):
        //:: include("custom/%s.Builder.java" % msg.name, msg=msg, has_parent=False)
        //:: #endif

    }
    //:: else:
    // no data members - do not support builder
    public ${msg.interface.name}.Builder createBuilder() {
        throw new UnsupportedOperationException("${impl_class} has no mutable properties -- builder unneeded");
    }
    //:: #endif


    final static Reader READER = new Reader();
    static class Reader implements OFMessageReader<${msg.interface.name}> {
        @Override
        public ${msg.interface.name} readFrom(ChannelBuffer bb) throws OFParseError {
//:: for prop in msg.members:
//:: if not prop.is_virtual and (prop.is_length_value or prop.is_field_length_value):
            int start = bb.readerIndex();
//::     break
//:: #endif
//:: #endfor
//:: fields_with_length_member = {}
//:: for prop in msg.members:
//:: if prop.is_virtual:
//::    continue
//:: elif prop.is_data:
            ${prop.java_type.public_type} ${prop.name} = ${prop.java_type.read_op(version, pub_type=True,
                    length=fields_with_length_member[prop.c_name] if prop.c_name in fields_with_length_member else None)};
//:: elif prop.is_pad:
            // pad: ${prop.length} bytes
            bb.skipBytes(${prop.length});
//:: elif prop.is_length_value:
            ${prop.java_type.public_type} ${prop.name} = ${prop.java_type.read_op(version, pub_type=True)};
            //:: if prop.is_fixed_value:
            if(${prop.name} != ${prop.value})
                throw new OFParseError("Wrong ${prop.name}: Expected=${prop.enum_value}(${prop.value}), got="+${prop.name});
            //:: else:
            if(${prop.name} < MINIMUM_LENGTH)
                throw new OFParseError("Wrong ${prop.name}: Expected to be >= " + MINIMUM_LENGTH + ", was: " + ${prop.name});
            //:: #endif
            if(bb.readableBytes() + (bb.readerIndex() - start) < ${prop.name}) {
                // Buffer does not have all data yet
                bb.readerIndex(start);
                return null;
            }
//:: elif prop.is_fixed_value:
            // fixed value property ${prop.name} == ${prop.value}
            ${prop.java_type.priv_type} ${prop.name} = ${prop.java_type.read_op(version, pub_type=False)};
            if(${prop.name} != ${prop.priv_value})
                throw new OFParseError("Wrong ${prop.name}: Expected=${prop.enum_value}(${prop.value}), got="+${prop.name});
//:: elif prop.is_field_length_value:
//::        fields_with_length_member[prop.member.field_name] = prop.name
            ${prop.java_type.public_type} ${prop.name} = ${prop.java_type.read_op(version, pub_type=True)};
//:: else:
    // fixme: todo ${prop.name}
//:: #endif
//:: #endfor
            //:: if msg.align:
            // align message to ${msg.align} bytes
            bb.skipBytes(((length + ${msg.align-1})/${msg.align} * ${msg.align} ) - length );
            //:: #endif

            //:: if msg.data_members:
            //:: if os.path.exists("%s/custom/%s.Reader_normalize_stanza.java" % (template_dir, msg.name)):
            //:: include("custom/%s.Reader_normalize_stanza.java" % msg.name, msg=msg, has_parent=False)
            //:: #endif
             return new ${impl_class}(
                    ${",\n                      ".join(
                         [ prop.name for prop in msg.data_members])}
                    );
            //:: else:
            return INSTANCE;
            //:: #endif
        }
    }

    public void writeTo(ChannelBuffer bb) {
        WRITER.write(bb, this);
    }

    final static Writer WRITER = new Writer();
    static class Writer implements OFMessageWriter<${impl_class}> {
        @Override
        public void write(ChannelBuffer bb, ${impl_class} message) {
//:: if not msg.is_fixed_length:
            int startIndex = bb.writerIndex();
//:: #endif
//:: fields_with_length_member = {}
//:: for prop in msg.members:
//:: if prop.c_name in fields_with_length_member:
            int ${prop.name}StartIndex = bb.writerIndex();
//:: #endif
//:: if prop.is_virtual:
//::    continue
//:: elif prop.is_data:
            ${prop.java_type.write_op(version, "message." + prop.name, pub_type=True)};
//:: elif prop.is_pad:
            // pad: ${prop.length} bytes
            bb.writeZero(${prop.length});
//:: elif prop.is_fixed_value:
            // fixed value property ${prop.name} = ${prop.value}
            ${prop.java_type.write_op(version, prop.priv_value, pub_type=False)};
//:: elif prop.is_length_value:
            // ${prop.name} is length of variable message, will be updated at the end
//:: if not msg.is_fixed_length:
            int lengthIndex = bb.writerIndex();
//:: #end
            ${prop.java_type.write_op(version, 0)};

//:: elif prop.is_field_length_value:
//::        fields_with_length_member[prop.member.field_name] = prop.name
            // ${prop.name} is length indicator for ${prop.member.field_name}, will be
            // udpated when ${prop.member.field_name} has been written
            int ${prop.name}Index = bb.writerIndex();
            ${prop.java_type.write_op(version, 0, pub_type=False)};
//:: else:
            // FIXME: todo write ${prop.name}
//:: #endif
//:: if prop.c_name in fields_with_length_member:
//::     length_member_name = fields_with_length_member[prop.c_name]
            // update field length member ${length_member_name}
            int ${prop.name}Length = bb.writerIndex() - ${prop.name}StartIndex;
            bb.setShort(${length_member_name}Index, ${prop.name}Length);
//:: #endif
//:: #endfor

//:: if not msg.is_fixed_length:
            // update length field
            int length = bb.writerIndex() - startIndex;
            bb.setShort(lengthIndex, length);
            //:: if msg.align:
            // align message to ${msg.align} bytes
            bb.writeZero( ((length + ${msg.align-1})/${msg.align} * ${msg.align}) - length);
            //:: #endif
//:: #end

        }
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("${msg.name}(");
        //:: for i, prop in enumerate(msg.data_members):
        //:: if i > 0:
        b.append(", ");
        //:: #endif
        b.append("${prop.name}=").append(${ "Arrays.toString(%s)" % prop.name if prop.java_type.is_array else prop.name });
        //:: #endfor
        b.append(")");
        return b.toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        //:: if len(msg.data_members) > 0:
        ${msg.name} other = (${msg.name}) obj;
        //:: #endif

        //:: for prop in msg.data_members:
        //:: if prop.java_type.is_primitive:
        if( ${prop.name} != other.${prop.name})
            return false;
        //:: elif prop.java_type.is_array:
        if (!Arrays.equals(${prop.name}, other.${prop.name}))
                return false;
        //:: else:
        if (${prop.name} == null) {
            if (other.${prop.name} != null)
                return false;
        } else if (!${prop.name}.equals(other.${prop.name}))
            return false;
        //:: #endif
        //:: #endfor
        return true;
    }

    @Override
    public int hashCode() {
        //:: if len(msg.data_members) > 0:
        final int prime = 31;
        //:: #endif
        int result = 1;

        //:: for prop in msg.data_members:
        //:: if prop.java_type.pub_type == 'long':
        result = prime *  (int) (${prop.name} ^ (${prop.name} >>> 32));
        //:: elif prop.java_type.is_primitive:
        result = prime * result + ${prop.name};
        //:: elif prop.java_type.is_array:
        result = prime * result + Arrays.hashCode(${prop.name});
        //:: else:
        result = prime * result + ((${prop.name} == null) ? 0 : ${prop.name}.hashCode());
        //:: #endif
        //:: #endfor
        return result;
    }


}
