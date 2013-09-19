:: # Copyright 2013, Big Switch Networks, Inc.
:: #
:: # LoxiGen is licensed under the Eclipse Public License, version 1.0 (EPL), with
:: # the following special exception:
:: #
:: # LOXI Exception
:: #
:: # As a special exception to the terms of the EPL, you may distribute libraries
:: # generated by LoxiGen (LoxiGen Libraries) under the terms of your choice, provided
:: # that copyright and licensing notices generated by LoxiGen are not altered or removed
:: # from the LoxiGen Libraries and the notice provided below is (i) included in
:: # the LoxiGen Libraries, if distributed in source code form and (ii) included in any
:: # documentation for the LoxiGen Libraries, if distributed in binary form.
:: #
:: # Notice: "Copyright 2013, Big Switch Networks, Inc. This library was generated by the LoxiGen Compiler."
:: #
:: # You may not use this file except in compliance with the EPL or LOXI Exception. You may obtain
:: # a copy of the EPL at:
:: #
:: # http://www.eclipse.org/legal/epl-v10.html
:: #
:: # Unless required by applicable law or agreed to in writing, software
:: # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
:: # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
:: # EPL for the specific language governing permissions and limitations
:: # under the EPL.
::
:: # TODO coalesce format strings
:: from loxi_ir import *
:: from py_gen.oftype import gen_unpack_expr
        if type(buf) == loxi.generic_util.OFReader:
            reader = buf
        else:
            reader = loxi.generic_util.OFReader(buf)
:: field_length_members = {}
:: for m in ofclass.members:
::     if type(m) == OFPadMember:
        reader.skip(${m.length})
::     elif type(m) == OFLengthMember:
        _${m.name} = ${gen_unpack_expr(m.oftype, 'reader', version=version)}
::     elif type(m) == OFFieldLengthMember:
::         field_length_members[m.field_name] = m
        _${m.name} = ${gen_unpack_expr(m.oftype, 'reader', version=version)}
::     elif type(m) == OFTypeMember:
        _${m.name} = ${gen_unpack_expr(m.oftype, 'reader', version=version)}
        assert(_${m.name} == ${m.value})
::     elif type(m) == OFDataMember:
::         if m.name in field_length_members:
::             reader_expr = 'reader.slice(_%s)' % field_length_members[m.name].name
::         else:
::             reader_expr = 'reader'
::         #endif
        obj.${m.name} = ${gen_unpack_expr(m.oftype, reader_expr, version=version)}
::     #endif
:: #endfor
:: if ofclass.has_external_alignment or ofclass.has_internal_alignment:
        reader.skip_align()
:: #endif
